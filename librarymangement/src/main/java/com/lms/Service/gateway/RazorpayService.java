package com.lms.Service.gateway;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.lms.Model.Payment;
import com.lms.Model.SubscriptionPlan;
import com.lms.Model.User;
import com.lms.Service.SubscriptionPlanService;
import com.lms.domain.PaymentType;
import com.lms.payload.response.PaymentLinkResponse;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RazorpayService {

    private final SubscriptionPlanService subscriptionPlanService;

        @Value("${razorpay.key.id}")
        private String razorpayKeyId;

        @Value("${razorpay.key.secret}")
        private String razorpayKeySecret;

        @Value("${razorpay.usd.to.php.rate}")
        private String usdToPhpRateConfig;

        @Value("${app.callback.base.url}")
        private String callbackBaseUrl;

    public PaymentLinkResponse createPaymentLink(User user, Payment payment) {

        try {
            RazorpayClient razorpayClient = new RazorpayClient(razorpayKeyId, razorpayKeySecret);

            BigDecimal amountInPeso = new BigDecimal(payment.getAmount().toString());

            BigDecimal usdToPhpRate = new BigDecimal(usdToPhpRateConfig);

            BigDecimal amountInUSD = amountInPeso.divide(usdToPhpRate, 2, RoundingMode.HALF_UP);

            Long finalAmountInUSDCents = amountInUSD.multiply(new BigDecimal("100")).longValueExact();

            JSONObject paymentLinkRequest = new JSONObject();
            paymentLinkRequest.put("amount", finalAmountInUSDCents);
            paymentLinkRequest.put("currency", "USD");
            paymentLinkRequest.put("description", payment.getDescription());

            JSONObject customer = new JSONObject();
            customer.put("name", user.getFullName());
            customer.put("email", user.getEmail());
            if (user.getPhone() != null) {
                customer.put("contact", user.getPhone());
            }

            paymentLinkRequest.put("customer", customer);

            JSONObject notify = new JSONObject();
            notify.put("email", true);
            notify.put("sms", user.getPhone() != null);
            paymentLinkRequest.put("notify", notify);

            paymentLinkRequest.put("reminder_enable", true);

            String successUrl = callbackBaseUrl + "/payment-success/" + payment.getId();
            //String cancelUrl = callbackBaseUrl + "/payment-cancel/" + payment.getId();

            paymentLinkRequest.put("callback_url", successUrl);
            paymentLinkRequest.put("callback_method", "get");

            JSONObject notes = new JSONObject();
            notes.put("user_id", user.getId());
            notes.put("payment_id", payment.getId());

            if (payment.getPaymentType() == PaymentType.MEMBERSHIP) {
                notes.put("subscription_id", payment.getSubscription().getId());
                notes.put("plan", payment.getSubscription().getPlan().getPlanCode());
                notes.put("type", PaymentType.MEMBERSHIP);
            } else if (payment.getPaymentType() == PaymentType.FINE) {
                //notes.put("fine_id", payment.getFine().getId());
                notes.put("type", PaymentType.FINE);
            }

            paymentLinkRequest.put("notes", notes);

            PaymentLink paymentLink = razorpayClient.paymentLink.create(paymentLinkRequest);

            PaymentLinkResponse response = new PaymentLinkResponse();
            response.setPayment_link_id(paymentLink.get("id"));
            response.setPayment_link_url(paymentLink.get("short_url"));
            return response;
        } catch (RazorpayException e) {
            throw new RuntimeException("Razorpay Exception:" + e.getMessage(), e);
        }

    }

    public JSONObject fetchPaymentDetails(String paymentId) throws Exception {
        
        try {
            RazorpayClient razorpayClient = new RazorpayClient(razorpayKeyId, razorpayKeySecret);

            com.razorpay.Payment rawPayment = razorpayClient.payments.fetch(paymentId);

            return new JSONObject(rawPayment.toString());
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch Razorpay payment details:" + e.getMessage(), e);
        }

    }

    public Boolean isValidPayment(String paymentId) {
       
        try {
            JSONObject paymentDetails = fetchPaymentDetails(paymentId);

            String status = paymentDetails.optString("status");
            Long amountInUSDCents = paymentDetails.optLong("amount");

            BigDecimal usdDecimals = new BigDecimal(amountInUSDCents).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);

            BigDecimal usdToPhpRate = new BigDecimal(usdToPhpRateConfig);

            BigDecimal exactAmountInPeso = usdDecimals.multiply(usdToPhpRate).setScale(2, RoundingMode.HALF_UP);

            System.out.println("Verified Webhook Amount - USD: $" + usdDecimals + " | PHP: ₱" + exactAmountInPeso);

            JSONObject notes = paymentDetails.getJSONObject("notes");

            String paymentType = notes.optString("type");

            if (!"captured".equalsIgnoreCase(status)) {
                return false;
            }

            if (paymentType.equals(PaymentType.MEMBERSHIP.toString())) {
                String planCode = notes.optString("plan");
                SubscriptionPlan subscriptionPlan = subscriptionPlanService.getBySubscriptionPlanCode(planCode);
                BigDecimal planPrice = new BigDecimal(String.valueOf(subscriptionPlan.getPrice()));
                return exactAmountInPeso.compareTo(planPrice) == 0;
            } else if (paymentType.equals(PaymentType.FINE.toString())) {
                Long fineId = notes.optLong("fine_id");

                return exactAmountInPeso.compareTo(BigDecimal.ZERO) > 0;
            }

            return false;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}

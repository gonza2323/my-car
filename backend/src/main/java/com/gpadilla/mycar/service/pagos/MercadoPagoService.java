package com.gpadilla.mycar.service.pagos;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.preference.*;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.preference.Preference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class MercadoPagoService {

    private final PaymentClient paymentClient;

    public MercadoPagoService(@Value("${app.mercadopago.access-token}") String accessToken) {
        MercadoPagoConfig.setAccessToken(accessToken);
        this.paymentClient = new PaymentClient();
    }

    public String createPreference(Long alquilerId, Double monto, String descripcion) throws MPException, MPApiException {
        PreferenceItemRequest item = PreferenceItemRequest.builder()
                    .id(alquilerId.toString())
                    .title("Alquiler de vehículo")
                    .description(descripcion)
                    .categoryId("services")
                    .quantity(1)
                    .currencyId("ARS")
                    .unitPrice(new BigDecimal(monto))
                    .build();

        PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                .success("https://mycar.gpadilla.com/success")
                .pending("https://mycar.gpadilla.com/pending")
                .failure("https://mycar.gpadilla.com/failure")
                .build();

        // No permitimos pagar en efectivo, así no queda pendiente el pago
        PreferencePaymentTypeRequest excludedType = PreferencePaymentTypeRequest.builder()
                .id("ticket").build();

        PreferencePaymentMethodsRequest paymentMethods =
                PreferencePaymentMethodsRequest.builder()
                        .excludedPaymentTypes(Arrays.asList(excludedType))
                        .build();

        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                .items(List.of(item))
                .paymentMethods(paymentMethods)
                .backUrls(backUrls)
                .autoReturn("approved")
                .notificationUrl("https://api.mycar.gpadilla.com/webhook/mercadopago")
                .externalReference(LocalDateTime.now().toString())
                .statementDescriptor("Gimnasio Sport")
                .build();

        PreferenceClient client = new PreferenceClient();

        Preference preference = client.create(preferenceRequest);

        return preference.getInitPoint();
    }

    public Payment getPaymentDetails(Long mpPaymentId) throws Exception {
        return paymentClient.get(mpPaymentId);
    }
}

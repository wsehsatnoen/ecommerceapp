package me.wsehsatnoen.ecommerceapp.controllers;


import me.wsehsatnoen.ecommerceapp.services.CheckoutServices;
import me.wsehsatnoen.ecommerceapp.services.PurchaseData;
import me.wsehsatnoen.ecommerceapp.services.PurchaseResponse;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping("/api/checkout")
public class CheckoutController {

    private CheckoutServices checkoutServices;

    public CheckoutController(CheckoutServices checkoutServices) {
        this.checkoutServices = checkoutServices;
    }

    @PostMapping("/purchase")
    public PurchaseResponse purchase(@RequestBody PurchaseData purchaseData) {

        return checkoutServices.checkout(purchaseData);
    }

}

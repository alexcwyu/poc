package net.alexyu.poc;

import java.util.Random;

/**
 * Created by alex on 3/4/17.
 */
public class ExchangeService {

    private Random random = new Random();

    public double getRate(String rate1, String rate2) {
        Utils.delay(1000l);
        return random.nextDouble();
    }


    public Quote convertToUSD(Quote quote , double rate) {
        return Quote.newQuote(quote.shop, quote.product, quote.price * rate, quote.discountCode, quote.discountedPrice * rate);
    }

}

package net.alexyu.poc;

import java.util.Random;

/**
 * Created by alex on 3/4/17.
 */
public class DiscountService {
    public static enum Code {
        NONE(0), SILVER(5), GOLD(10), PLATINUM(15), DIAMOND(20);

        private final int percentage;

        Code(int percentage) {
            this.percentage = percentage;
        }
    }

    private Random random = new Random();

    public Code getRandomDiscount() {
        Utils.delay(1000l);
        return Code.values()[random.nextInt(Code.values().length)];
    }

    public Quote getAndApplyDiscount(Quote quote) {
        Code code = getRandomDiscount();
        return calcDiscountedQuote(quote, code);
    }

    public Quote applyDiscount(Quote quote) {
        return calcDiscountedQuote(quote, quote.discountCode);
    }

    private Quote calcDiscountedQuote(Quote quote, Code code) {
        double discountedPrice = quote.price * (100 - code.percentage) / 100;
        return Quote.newQuote(quote, code, discountedPrice);
    }

}

package net.alexyu.poc;

/**
 * Created by alex on 3/4/17.
 */
public class Quote {

    public final String shop;
    public final String product;
    public final double price;
    public final DiscountService.Code discountCode;
    public final double discountedPrice;

    public Quote(String shop, String product, double price) {
        this.shop = shop;
        this.product = product;
        this.price = price;
        this.discountCode = null;
        this.discountedPrice = 0;
    }

    public Quote(String shop, String product, double price, DiscountService.Code discountCode) {
        this.shop = shop;
        this.product = product;
        this.price = price;
        this.discountCode = discountCode;
        this.discountedPrice = 0;
    }


    public Quote(String shop, String product, double price, DiscountService.Code discountCode, double discountedPrice) {
        this.shop = shop;
        this.product = product;
        this.price = price;
        this.discountCode = discountCode;
        this.discountedPrice = discountedPrice;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Quote quote = (Quote) o;

        if (Double.compare(quote.price, price) != 0) return false;
        if (Double.compare(quote.discountedPrice, discountedPrice) != 0) return false;
        if (shop != null ? !shop.equals(quote.shop) : quote.shop != null) return false;
        if (product != null ? !product.equals(quote.product) : quote.product != null) return false;
        return discountCode == quote.discountCode;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = shop != null ? shop.hashCode() : 0;
        result = 31 * result + (product != null ? product.hashCode() : 0);
        temp = Double.doubleToLongBits(price);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (discountCode != null ? discountCode.hashCode() : 0);
        temp = Double.doubleToLongBits(discountedPrice);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Quote{" +
                "shop='" + shop + '\'' +
                ", product='" + product + '\'' +
                ", price=" + price +
                ", discountCode=" + discountCode +
                ", discountedPrice=" + discountedPrice +
                '}';
    }

    public static Quote newQuote(String shop, String product, double price) {
        return new Quote(shop, product, price);
    }


    public static Quote newQuote(Quote quote, DiscountService.Code discountCode, double discountedPrice) {
        return new Quote(quote.shop, quote.product, quote.price, discountCode, discountedPrice);
    }

    public static Quote newQuote(String shop, String product, double price, DiscountService.Code discountCode, double discountedPrice) {
        return new Quote(shop, product, price, discountCode, discountedPrice);
    }
}

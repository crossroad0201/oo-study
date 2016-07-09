package hiruben.oo.cleaning.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 「チケット」です。
 */
public class Ticket {
  /** 整理番号 */
  public final String referenceNumber;
  /** 預けたクリーニング店名 */
  public final String shopName;
  /** 預けたお客さん名 */
  public final String customerName;
  /** 預けたお客さん電話番号 */
  public final String customerPhoneNumber;
  /** 預けたクリーニング品 */
  public final TicketItem[] items;

  Ticket(String number, String shopName, String customerName, String customerPhoneNumber, TicketItem... items) {
    this.referenceNumber = number;
    this.shopName = shopName;
    this.customerName = customerName;
    this.customerPhoneNumber = customerPhoneNumber;
    this.items = items;
  }

  /**
   * 未返却のクリーニング品を返します。
   *
   * @return クリーニング品のリスト
   */
  public TicketItem[] remainingItems() {
    List<TicketItem> remaining = new ArrayList<>();
    for (TicketItem i : items) {
      if (!i.collected) {
        remaining.add(i);
      }
    }
    return remaining.toArray(new TicketItem[0]);
  }

  /**
   * 未返却のクリーニング品があるかを判定します。
   *
   * @param item  クリーニング品
   * @return true:未返却あり / false:未返却なし
   */
  boolean hasRemaining(CleaningItem item) {
    for (TicketItem i : remainingItems()) {
      if (i.item.equals(item)) {
        return true;
      }
    }
    return false;
  }

  /**
   * クリーニング品を、返却済みとしてマークします。
   *
   * @param item クリーニング品
   * @return 自身
   */
  Ticket markAsCollected(CleaningItem item) {
    for (TicketItem i : remainingItems()) {
      if (i.item.equals(item)) {
        i.markAsCollected();
      }
    }
    return this;
  }

  /**
   * このチケットのクリーニング品を、すべて返却したかどうかを判定します。
   *
   * @return tue:すべて返却済み / false:まだ未返却がある
   */
  boolean isDone() {
    return remainingItems().length == 0;
  }

  @Override
  public String toString() {
    StringBuilder s = new StringBuilder()
        .append("--- チケット --------------------------------------------\n")
        .append(String.format("整理番号 : %s\n", referenceNumber))
        .append(String.format("店舗 : %s\n", shopName))
        .append(String.format("お客さま名 : %s\n", customerName))
        .append(String.format("お客さま電話番号 : %s\n", customerPhoneNumber));

    s.append("明細 : \n");
    for (TicketItem item : items) {
      s.append(String.format("  %s\n", item));
    }

    s.append("---------------------------------------------------------");
    return s.toString();
  }

  /**
   * 「チケット明細」です。
   */
  public static class TicketItem {
    /** クリーニング品 */
    public final CleaningItem item;
    /** 価格 */
    public final int price;
    /** 返却済み */
    public boolean collected = false;

    TicketItem(CleaningItem item, int price) {
      this.item = item;
      this.price = price;
    }

    /**
     * この明細を、返却済みにします。
     *
     * @return 自身
     */
    TicketItem markAsCollected() {
      this.collected = true;
      return this;
    }

    @Override
    public String toString() {
      return String.format("%s %s %s", item, price, collected ? "返却済" : "返却未");
    }
  }
}

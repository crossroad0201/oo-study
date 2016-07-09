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
  public final CleaningItem[] items;
  private final List<CleaningItem> remainingItems = new ArrayList<>();

  Ticket(String number, String shopName, String customerName, String customerPhoneNumber, CleaningItem... items) {
    this.referenceNumber = number;
    this.shopName = shopName;
    this.customerName = customerName;
    this.customerPhoneNumber = customerPhoneNumber;
    this.items = items;
    this.remainingItems.addAll(Arrays.asList(items));
  }

  /**
   * クリーニング品が、返却済みかどうかを判定します。
   *
   * @param item  クリーニング品
   * @return true:返却済み / false:返却未済
   */
  boolean isRemaining(CleaningItem item) {
    return remainingItems.contains(item);
  }

  /**
   * 未返却のクリーニング品を返します。
   *
   * @return クリーニング品のリスト
   */
  public CleaningItem[] remainingItems() {
    return remainingItems.toArray(new CleaningItem[0]);
  }

  /**
   * クリーニング品を、返却済みとしてマークします。
   *
   * @param item クリーニング品
   * @return 自身
   */
  Ticket markAsCollected(CleaningItem item) {
    remainingItems.remove(item);
    return this;
  }

  /**
   * このチケットのクリーニング品を、すべて返却したかどうかを判定します。
   *
   * @return tue:すべて返却済み / false:まだ未返却がある
   */
  boolean isDone() {
    return remainingItems.isEmpty();
  }
}

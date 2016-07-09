package hiruben.oo.cleaning.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 「注文」です。
 */
class Order {
  /** 整理番号 */
  String referenceNumber;
  /** 預かったクリーニング店 */
  CleaningShop shop;
  /** 注文したお客さん */
  Customer customer;
  /** 預かったクリーニング品 */
  Map<CleaningItem, ProcessState> items = new LinkedHashMap<>();

  Order(String referenceNumber, CleaningShop shop, Customer customer, CleaningItem... items) {
    this.referenceNumber = referenceNumber;
    this.shop = shop;
    this.customer = customer;
    for (CleaningItem item : items) {
      this.items.put(item, ProcessState.ACCEPTED);
    }
  }

  /**
   * この注文から、チケットを発行します。
   *
   * @return チケット
   */
  Ticket issueTicket() {
    return new Ticket(
      referenceNumber,
      shop.name,
      customer.name, customer.phoneNumber,
      items.keySet().toArray(new CleaningItem[0])
    );
  }

  /**
   * この注文のクリーニング品の加工を完了します。
   *
   * @param item クリーニング品
   * @return 自身
   */
  Order completeProcess(CleaningItem item) {
    items.put(item, ProcessState.COMPLETED);
    return this;
  }

  /**
   * この注文の、加工が完了したクリーニング品を返します。
   *
   * @return クリーニング品のリスト
   */
  CleaningItem[] completedItems() {
    List<CleaningItem> completeds = new ArrayList<>();
    for (Map.Entry<CleaningItem, ProcessState> i : items.entrySet()) {
      if (i.getValue() == ProcessState.COMPLETED) {
        completeds.add(i.getKey());
      }
    }
    return completeds.toArray(new CleaningItem[0]);
  }
}

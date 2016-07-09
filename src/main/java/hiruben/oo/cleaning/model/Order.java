package hiruben.oo.cleaning.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 「注文」です。
 */
class Order {
  /** 整理番号 */
  final String referenceNumber;
  /** 預かったクリーニング店 */
  final CleaningShop shop;
  /** 注文したお客さん */
  final Customer customer;
  /** 預かったクリーニング品 */
  final List<OrderItem> items = new ArrayList<>();

  Order(String referenceNumber, CleaningShop shop, Customer customer, CleaningItem... items) {
    this.referenceNumber = referenceNumber;
    this.shop = shop;
    this.customer = customer;
    for (CleaningItem item : items) {
      int price = 0;
      for (Process p : item.processes) {
        price += shop.priceList.findMenu(p, item.kind).price;
      }
      this.items.add(new OrderItem(item, price));
    }
  }

  /**
   * この注文から、チケットを発行します。
   *
   * @return チケット
   */
  Ticket issueTicket() {
    List<Ticket.TicketItem> ticketItems = new ArrayList<>();
    for (OrderItem item : items) {
      ticketItems.add(new Ticket.TicketItem(item.item, item.price));
    }

    return new Ticket(
      referenceNumber,
      shop.name,
      customer.name, customer.phoneNumber,
      ticketItems.toArray(new Ticket.TicketItem[0])
    );
  }

  /**
   * この注文のクリーニング品の加工を完了します。
   *
   * @param item クリーニング品
   * @return 自身
   */
  Order completeProcess(CleaningItem item) {
    for (OrderItem i : items) {
      if (i.item.equals(item) && i.state == ProcessState.ACCEPTED) {
        i.completeProcess();
        return this;
      }
    }
    throw new RuntimeException(String.format("この注文には、該当する未加工のクリーニング品はありません。 %s", item));
  }

  /**
   * この注文の、加工が完了したクリーニング品を返します。
   *
   * @return クリーニング品のリスト
   */
  CleaningItem[] completedItems() {
    List<CleaningItem> completeds = new ArrayList<>();
    for (OrderItem item : items) {
      if (item.state == ProcessState.COMPLETED) {
        completeds.add(item.item);
      }
    }
    return completeds.toArray(new CleaningItem[0]);
  }

  /**
   * 「注文明細」です。
   */
  private static class OrderItem {
    /** クリーニング品 */
    final CleaningItem item;
    /** 価格 */
    final int price;
    /** 加工状態 */
    ProcessState state;

    OrderItem(CleaningItem item, int price) {
      this.item = item;
      this.price = price;
      this.state = ProcessState.ACCEPTED;
    }

    /**
     * 加工済みにします。
     *
     * @return 自身
     */
    OrderItem completeProcess() {
      state = ProcessState.COMPLETED;
      return this;
    }
  }
}

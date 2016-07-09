package hiruben.oo.cleaning.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 「注文」です。
 */
public class Order {
  /** 整理番号 */
  public final String referenceNumber;
  /** 預かったクリーニング店 */
  public final CleaningShop shop;
  /** 注文したお客さん */
  public final Customer customer;
  /** 預かったクリーニング品 */
  public final OrderItem[] items;

  Order(String referenceNumber, CleaningShop shop, Customer customer, CleaningItem... items) {
    this.referenceNumber = referenceNumber;
    this.shop = shop;
    this.customer = customer;

    List<OrderItem> orderItems = new ArrayList<>();
    for (CleaningItem item : items) {
      orderItems.add(new OrderItem(item, calcSubtotalPrice(item)));
    }
    this.items = orderItems.toArray(new OrderItem[0]);
  }

  /**
   * クリーニング品ごとの料金の小計を計算します。
   *
   * @param item クリーニング品
   * @return 料金
   */
  private int calcSubtotalPrice(CleaningItem item) {
    int price = 0;
    for (Process process : item.processes) {
      Optional<PriceList.Menu> menu = shop.priceList.findMenu(process, item.kind);
      if (!menu.isPresent()) {
        throw new RuntimeException(String.format("メニューがありません。%s %s", item.kind, process));
      }
      price += menu.get().price;
    }
    return price;
  }

  /**
   * この注文のクリーニング品の加工を完了します。
   *
   * @param item クリーニング品
   * @return 自身
   */
  Order completeProcess(CleaningItem item) {
    for (OrderItem i : items) {
      if (i.item.equals(item) && i.state == ItemState.ACCEPTED) {
        i.processed();
        return this;
      }
    }
    throw new RuntimeException(String.format("この注文には、該当する未加工のクリーニング品はありません。 %s", item));
  }

  /**
   * この注文の、加工済の明細を返します。
   *
   * @return 明細のリスト
   */
  OrderItem[] processedItems() {
    List<OrderItem> completeds = new ArrayList<>();
    for (OrderItem item : items) {
      if (item.state == ItemState.PROCESSED) {
        completeds.add(item);
      }
    }
    return completeds.toArray(new OrderItem[0]);
  }

  /**
   * この注文の、未返却の明細を返します。
   *
   * @return 明細のリスト
   */
  public OrderItem[] remainingItems() {
    List<OrderItem> completeds = new ArrayList<>();
    for (OrderItem item : items) {
      if (item.state != ItemState.COLLECTED) {
        completeds.add(item);
      }
    }
    return completeds.toArray(new OrderItem[0]);
  }

  /**
   * 「注文明細」です。
   */
  public static class OrderItem {
    /** クリーニング品 */
    public final CleaningItem item;
    /** 価格 */
    public final int price;
    /** 加工状態 */
    public ItemState state;

    OrderItem(CleaningItem item, int price) {
      this.item = item;
      this.price = price;
      this.state = ItemState.ACCEPTED;
    }

    /**
     * 加工済みにします。
     *
     * @return 自身
     */
    OrderItem processed() {
      state = ItemState.PROCESSED;
      return this;
    }

    /**
     * 返却済みにします。
     *
     * @return 自身
     */
    OrderItem collected() {
      state = ItemState.COLLECTED;
      return this;
    }
  }
}

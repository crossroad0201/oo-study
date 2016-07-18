package hiruben.oo.cleaning.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

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
    for (ProcessKind process : item.processes) {
      Optional<PriceList.Menu> menu = shop.priceList.findMenu(process, item.kind);
      if (!menu.isPresent()) {
        throw new RuntimeException(String.format("メニューがありません。%s %s", item.kind, process));
      }
      price += menu.get().price;
    }
    return price;
  }

  /**
   * この注文の、受付済の明細を返します。
   *
   * @return 明細のリスト
   */
  OrderItem[] acceptedItems() {
    return filteredItemsBy(i -> i.state == ItemState.ACCEPTED);
  }

  /**
   * この注文の、加工済の明細を返します。
   *
   * @return 明細のリスト
   */
  OrderItem[] processedItems() {
    return filteredItemsBy(i -> i.state == ItemState.PROCESSED);
  }

  /**
   * この注文の、未返却の明細を返します。
   *
   * @return 明細のリスト
   */
  public OrderItem[] remainingItems() {
    return filteredItemsBy(i -> i.state != ItemState.RETURNED);
  }

  private OrderItem[] filteredItemsBy(Predicate<OrderItem> predicate) {
    List<OrderItem> founds = new ArrayList<>();
    for (OrderItem item : items) {
      if (predicate.test(item)) {
        founds.add(item);
      }
    }
    return founds.toArray(new OrderItem[0]);
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
    OrderItem markAsProcessed() {
      state = ItemState.PROCESSED;
      return this;
    }

    /**
     * 返却済みにします。
     *
     * @return 自身
     */
    OrderItem markAsReturned() {
      state = ItemState.RETURNED;
      return this;
    }
  }
}

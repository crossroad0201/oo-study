package hiruben.oo.cleaning.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 「クリーニング店」です。
 */
public class CleaningShop {
  /** 店名 */
  public final String name;
  /** 価格表 */
  public final PriceList priceList;

  private final Map<String, Order> orders = new LinkedHashMap<>();

  public CleaningShop(String name, PriceList priceList) {
    this.name = name;
    this.priceList = priceList;
  }

  /**
   * クリーニング品を預かります。
   *
   * @param customer お客さん
   * @param items クリーニング品
   * @return 注文
   */
  public Order acceptCleaningItem(Customer customer, CleaningItem... items) {
    for (CleaningItem item : items) {
      for (ProcessKind process : item.processes) {
        if (!priceList.findMenu(process, item.kind).isPresent()) {
          throw new RuntimeException(String.format("メニューがありません。%s %s", item.kind, process));
        }
      }
    }

    Order order = new Order(nextReferenceNumber(), this, customer, items);
    orders.put(order.referenceNumber, order);

    return order;
  }

  private String nextReferenceNumber() {
    return "123";
  }

  /**
   * 加工が完了したクリーニング品を搬入します。
   *
   * @param referenceNumber 整理番号
   * @param item クリーニング品
   * @return 注文
   */
  public Order receiveProcessedItem(String referenceNumber, CleaningItem item) {
    Order order = findOrder(referenceNumber);

    for (Order.OrderItem oItem : order.acceptedItems()) {
      if (oItem.item.equals(item)) {
        oItem.markAsProcessed();
      }
    }

    return order;
  }

  /**
   * クリーニング品を返します。
   *
   * @param referenceNumber 整理番号
   * @return 返却物
   */
  public ReturnResult returnCleaningItem(String referenceNumber) {
    Order order = findOrder(referenceNumber);

    List<CleaningItem> cleanItems = new ArrayList<>();
    for (Order.OrderItem item : order.processedItems()) {
      cleanItems.add(item.item);
      item.markAsReturned();
    }

    if (order.remainingItems().length == 0) {
      orders.remove(order.referenceNumber);
    }

    return new ReturnResult(order, cleanItems.toArray(new CleaningItem[0]));
  }

  private Order findOrder(String referenceNumber) {
    if (!orders.containsKey(referenceNumber)) {
      throw new RuntimeException(String.format("整理番号 %s の注文はありません。", referenceNumber));
    }

    return orders.get(referenceNumber);
  }

  @Override
  public String toString() {
    StringBuilder s = new StringBuilder()
        .append(String.format("店舗 : %s\n", name))
        .append(priceList);

    return s.toString();
  }

  /**
   * 返却物です。
   */
  public static class ReturnResult {
    /** 注文 */
    public final Order order;
    /** 今回仕上がっていたクリーニング品 */
    public final CleaningItem[] processedItems;

    private ReturnResult(Order order, CleaningItem... items) {
      this.order = order;
      this.processedItems = items;
    }
  }

}

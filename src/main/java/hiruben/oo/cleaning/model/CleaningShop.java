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
   * @return チケット
   */
  public Ticket accept(Customer customer, CleaningItem... items) {
    for (CleaningItem item : items) {
      for (Process process : item.processes) {
        if (priceList.findMenu(process, item.kind) == null) {
          throw new RuntimeException(String.format("メニューがありません。%s %s", item.kind, process));
        }
      }
    }

    Order order = new Order("123", this, customer, items);
    orders.put(order.referenceNumber, order);

    return order.issueTicket();
  }

  Order findOrder(String referenceNumber) {
    if (!orders.containsKey(referenceNumber)) {
      throw new RuntimeException(String.format("整理番号 %s の注文はありません。", referenceNumber));
    }

    return orders.get(referenceNumber);
  }

  /**
   * クリーニング品を返します。
   *
   * @param ticket チケット
   * @return 返却物
   */
  public CollectResult collect(Ticket ticket) {
    Order order = findOrder(ticket.referenceNumber);

    /* 加工が完了しているクリーニング品について、チケットから消しこんでいく */
    List<CleaningItem> items = new ArrayList<>();
    for (CleaningItem item : order.completedItems()) {
      if (ticket.hasRemaining(item)) {
        ticket.markAsCollected(item);
        items.add(item);
      }
    }

    if (ticket.isDone()) {
      orders.remove(ticket.referenceNumber);
      return CollectResult.noRemainingItems(items.toArray(new CleaningItem[0]));
    } else {
      return CollectResult.remainingItems(ticket, items.toArray(new CleaningItem[0]));
    }
  }

  public CleaningShop completeProcess(String referenceNumber, CleaningItem item) {
    Order order = findOrder(referenceNumber);
    order.completeProcess(item);
    return this;
  }

  @Override
  public String toString() {
    StringBuilder s = new StringBuilder()
        .append(String.format("店舗 : %s\n", name))
        .append(priceList);

    return s.toString();
  }
}

package hiruben.oo.cleaning;

import hiruben.oo.cleaning.model.Order;
import hiruben.oo.cleaning.model.Process;

import java.util.ArrayList;
import java.util.List;

/**
 * プリンタです。
 *
 */
public class Printer {

  /**
   * チケットを出力します。
   *
   * @param order 注文
   * @return チケット
   */
  public String printTicket(Order order) {
    StringBuilder s = new StringBuilder()
        .append("+--- チケット ----------------------------------\n")
        .append(String.format("| 整理番号　 : %s\n", order.referenceNumber))
        .append(String.format("| 店舗　　　 : %s\n", order.shop.name))
        .append(String.format("| お客様名　 : %s\n", order.customer.name))
        .append(String.format("| お客様電話 : %s\n", order.customer.phoneNumber));

    s.append("| 明細 : \n");
    for (Order.OrderItem item : order.items) {
      s.append(String.format("|   %s %s %s\n", item.item, item.price, item.state));
    }

    s.append("+-----------------------------------------------");
    return s.toString();
  }

  /**
   * タグを出力します。
   *
   * @param order 注文
   * @return タグのリスト
   */
  public String[] printTags(Order order) {
    List<String> tags = new ArrayList<>();
    for (Order.OrderItem item : order.items) {
      for (Process process : item.item.processes) {
        StringBuilder s = new StringBuilder()
            .append("+--- タグ ----------------------------------------------\n")
            .append(String.format("| 整理番号 : %s / 品目 : %s /  加工 : %s\n", order.referenceNumber, item.item.kind, process))
            .append("+-------------------------------------------------------");
        tags.add(s.toString());
      }
    }
    return tags.toArray(new String[0]);
  }

}

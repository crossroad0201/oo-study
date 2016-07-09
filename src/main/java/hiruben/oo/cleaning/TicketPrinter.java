package hiruben.oo.cleaning;

import hiruben.oo.cleaning.model.Order;

/**
 * チケットを出力するプリンタです。
 *
 */
public class TicketPrinter {

  /**
   * チケットを出力します。
   *
   * @param order 注文
   * @return チケット
   */
  public String printTicket(Order order) {
    StringBuilder s = new StringBuilder()
        .append("--------------------------------------\n")
        .append(String.format("整理番号　 : %s\n", order.referenceNumber))
        .append(String.format("店舗　　　 : %s\n", order.shop.name))
        .append(String.format("お客様名　 : %s\n", order.customer.name))
        .append(String.format("お客様電話 : %s\n", order.customer.phoneNumber));

    s.append("明細 : \n");
    for (Order.OrderItem item : order.items) {
      s.append(String.format("  %s %s %s\n", item.item, item.price, item.state));
    }

    s.append("--------------------------------------");
    return s.toString();
  }

}

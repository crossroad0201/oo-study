package hiruben.oo.cleaning;

import hiruben.oo.cleaning.model.*;
import hiruben.oo.cleaning.model.Process;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class UseCaseTest {

  @Test
  public void 徳川さんの場合() {
    TicketPrinter printer = new TicketPrinter();

    /*
     * 背広のドライクリーニング代金は800円、ワイシャツのドライは200円、水洗いは150円、防虫加工は品目にかかわらず一律500円である。
     */
    CleaningShop shop = new CleaningShop("テスト店",
        new PriceList()
          .addMenu(Process.DRY_CLEANING, ItemKind.SUIT, 800)
          .addMenu(Process.DRY_CLEANING, ItemKind.SHIRT, 200)
          .addMenu(Process.WATER_WASH, ItemKind.SHIRT, 150)
          .addMenu(Process.MOTHPROOFING, 500)
    );
    System.out.println(shop);


    /*
     * 徳川さんから4月18日に、背広のドライクリーニングと防虫加工、ワイシャツの水洗いと防虫加工でお預かりした。
     */
    Customer tokugawa = new Customer("徳川", "06-1234-5678");
    CleaningItem suit = new CleaningItem(ItemKind.SUIT, Process.DRY_CLEANING, Process.MOTHPROOFING);
    CleaningItem shirt = new CleaningItem(ItemKind.SHIRT, Process.WATER_WASH, Process.MOTHPROOFING);

    Order order = shop.accept(tokugawa, suit, shirt);
    System.out.println("チケット");
    System.out.println(printer.printTicket(order));

    assertThat("チケットに整理番号が記載されている", order.referenceNumber, equalTo("123"));
    assertThat("チケットに店名が記載されている", order.shop.name, equalTo("テスト店"));
    assertThat("チケットに顧客名が記載されている", order.customer.name, equalTo("徳川"));
    assertThat("チケットに顧客電話番号が記載されている", order.customer.phoneNumber, equalTo("06-1234-5678"));
    assertThat("チケットの未返却品が、預けたクリーニング品と一致する", order.remainingItems().length, equalTo(2));

    shop.deliverProcessedItem(order.referenceNumber, shirt);


    /*
     * 徳川さんが4月21日に取りに見えたので、すでにできていたワイシャツだけをお返しした。
     */
    CollectResult collected = shop.collect(order.referenceNumber);
    System.out.println("チケット");
    System.out.println(printer.printTicket(collected.order));
    System.out.println("返却されたクリーニング品");
    for (CleaningItem i : collected.collectedItems) {
      System.out.println(String.format("  %s\n", i));
    }

    assertThat("返却されたチケットの未返却品が、まだ受け取っていないクリーニング品と一致する", collected.order.remainingItems().length, equalTo(1));
    assertThat("返却されたクリーニング品が一致する", collected.collectedItems.length, equalTo(1));
  }

}

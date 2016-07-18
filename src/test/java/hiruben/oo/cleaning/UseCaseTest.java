package hiruben.oo.cleaning;

import hiruben.oo.cleaning.model.*;
import hiruben.oo.cleaning.model.ProcessKind;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class UseCaseTest {

  @Test
  public void 徳川さんの場合() {
    Printer printer = new Printer();

    /*
     * 背広のドライクリーニング代金は800円、ワイシャツのドライは200円、水洗いは150円、防虫加工は品目にかかわらず一律500円である。
     */
    CleaningShop shop = new CleaningShop("テスト店",
        new PriceList()
          .addMenu(ProcessKind.DRY_CLEANING, ItemKind.SUIT, 800)
          .addMenu(ProcessKind.DRY_CLEANING, ItemKind.SHIRT, 200)
          .addMenu(ProcessKind.WATER_WASH, ItemKind.SHIRT, 150)
          .addMenu(ProcessKind.MOTHPROOFING, 500)
    );
    System.out.println(shop);


    /*
     * 徳川さんから4月18日に、背広のドライクリーニングと防虫加工、ワイシャツの水洗いと防虫加工でお預かりした。
     */
    System.out.println("");
    System.out.println("==== 4/18 預かり =============================================================================");

    Customer tokugawa = new Customer("徳川", "06-1234-5678");
    CleaningItem sebiro = new CleaningItem(ItemKind.SUIT, ProcessKind.DRY_CLEANING, ProcessKind.MOTHPROOFING);
    CleaningItem yShirt = new CleaningItem(ItemKind.SHIRT, ProcessKind.WATER_WASH, ProcessKind.MOTHPROOFING);

    // 預かり
    Order order = shop.acceptCleaningItem(tokugawa, sebiro, yShirt);

    // チケットを出力
    System.out.println(printer.printTicket(order));

    assertThat("チケットに整理番号が記載されている", order.referenceNumber, equalTo("123"));
    assertThat("チケットに店名が記載されている", order.shop.name, equalTo("テスト店"));
    assertThat("チケットに顧客名が記載されている", order.customer.name, equalTo("徳川"));
    assertThat("チケットに顧客電話番号が記載されている", order.customer.phoneNumber, equalTo("06-1234-5678"));
    assertThat("チケットの未返却品が、預けたクリーニング品と一致する", order.remainingItems().length, equalTo(2));

    // タグを出力
    String[] tags = printer.printTags(order);
    System.out.println("");
    for (String tag : tags) {
      System.out.println(tag);
    }

    // 加工が終わったシャツを、クリーニング店に納入。
    shop.receiveProcessedItem(order.referenceNumber, yShirt);


    /*
     * 徳川さんが4月21日に取りに見えたので、すでにできていたワイシャツだけをお返しした。
     */
    System.out.println("");
    System.out.println("==== 4/21 お返し =============================================================================");

    // お返し
    CleaningShop.ReturnResult result = shop.returnCleaningItem(order.referenceNumber);

    assertThat("返却されたチケットの未返却品が、まだ受け取っていないクリーニング品と一致する", result.order.remainingItems().length, equalTo(1));
    assertThat("返却されたクリーニング品が一致する", result.processedItems.length, equalTo(1));

    // チケットを出力
    System.out.println(printer.printTicket(result.order));

    System.out.println("");
    System.out.println("返却されたクリーニング品");
    for (CleaningItem i : result.processedItems) {
      System.out.println(String.format("  %s", i));
    }
  }

}

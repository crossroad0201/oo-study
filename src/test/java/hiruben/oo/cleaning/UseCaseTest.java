package hiruben.oo.cleaning;

import hiruben.oo.cleaning.model.*;
import hiruben.oo.cleaning.model.Process;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class UseCaseTest {

  @Test
  public void 徳川さんの場合() {
    CleaningShop shop = new CleaningShop("テスト店");

    /*
     * 徳川さんから4月18日に、背広のドライクリーニングと防虫加工、ワイシャツの水洗いと防虫加工でお預かりした。
     */
    Customer tokugawa = new Customer("徳川", "06-1234-5678");
    CleaningItem suit = new CleaningItem("徳川さんの背広", Item.SUIT, Process.DRY_CLEANING, Process.MOTHPROOFING);
    CleaningItem shirt = new CleaningItem("徳川さんのワイシャツ", Item.SHIRT, Process.WATER_WASH, Process.MOTHPROOFING);

    Ticket ticket = shop.accept(tokugawa, suit, shirt);

    assertThat("チケットに整理番号が記載されている", ticket.referenceNumber, equalTo("123"));
    assertThat("チケットに店名が記載されている", ticket.shopName, equalTo("テスト店"));
    assertThat("チケットに顧客名が記載されている", ticket.customerName, equalTo("徳川"));
    assertThat("チケットに顧客電話番号が記載されている", ticket.customerPhoneNumber, equalTo("06-1234-5678"));
    assertThat("チケットの未返却品が、預けたクリーニング品と一致する", ticket.remainingItems().length, equalTo(2));

    shop.completeProcess(ticket.referenceNumber, shirt);

    /*
     * 徳川さんが4月21日に取りに見えたので、すでにできていたワイシャツだけをお返しした。
     */
    CollectResult collected = shop.collect(ticket);

    assertThat("チケットが返却されている", collected.ticket, notNullValue());
    assertThat("返却されたチケットの未返却品が、まだ受け取っていないクリーニング品と一致する", collected.ticket.remainingItems().length, equalTo(1));
    assertThat("返却されたクリーニング品が一致する", collected.items.length, equalTo(1));
  }

}

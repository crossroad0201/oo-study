package hiruben.oo.cleaning;

import org.junit.Test;
import static org.junit.Assert.*;

public class UseCaseTest {

	@Test
	public void 徳川さんの場合() {
		CleaningShop shop = new CleaningShop("テスト店");

		Customer tokugawa = new Customer("徳川", "06-1234-5678");

		CleaningItem suit = new CleaningItem(Item.SUIT, Process.DRY_CLEANING, Process.MOTHPROOGING);
		CleaningItem shirt = new CleaningItem(Item.SHIRT, Process.WATER_WATH, Process.MOTHPROOGING);

		Ticket ticket = shop.accept(tokugawa, suit, shirt);

		assertThat(ticket.number, equalTo(123));
		assertThat(ticket.shopName, equalTo("テスト店"));
		assertThat(ticket.customerName, equalTo("徳川"));
		assertThat(ticket.customerPhoneNumber, equalTo("06-1234-5678"));
		assertThat(ticket.remainingItems, hasSize(2));

		shop.complete(shirt);

		CollectResult collected = shop.collect(ticket);

		assertThat(collected.ticket.number, equalTo(123));
		assertThat(collected.ticket.remainingItems, hasSize(1));
		assertThat(collected.items, hasSize(1));
	}

}

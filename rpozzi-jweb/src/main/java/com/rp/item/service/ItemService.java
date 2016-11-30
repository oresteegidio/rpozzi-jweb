package com.rp.item.service;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.rp.item.model.Item;

@Path("/items")
public class ItemService {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Item> getItems() {
		List<Item> list = null;
		try {
			list = new ArrayList<Item>();
			Item item1 = new Item();
			item1.setId(111111);
			item1.setName("TEST");
			Item item2 = new Item();
			item2.setId(112311);
			item2.setName("TEST 2");
			list.add(item1);
			list.add(item2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

}
package org.mo.bots.PizzaBot.data;

import org.mo.bots.PizzaBot.objects.ClientGroup;
import org.mo.bots.PizzaBot.objects.Product;
import org.mo.bots.PizzaBot.objects.User;

import java.util.List;

public interface DataProvider {

    User getUser(long id, String phone);

    Product getProductById(int id);

    List<ClientGroup> getClientGroups();

    List<User> getUsersInGroup(long id);

}

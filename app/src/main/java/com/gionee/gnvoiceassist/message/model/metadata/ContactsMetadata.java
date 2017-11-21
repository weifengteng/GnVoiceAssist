package com.gionee.gnvoiceassist.message.model.metadata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by liyingheng on 11/9/17.
 */

public class ContactsMetadata extends Metadata {

    public ContactsMetadata() {

    }

    public ContactsMetadata(String name, String... numbers) {
        this.name = name;
        number = new ArrayList<>();
        number.addAll(Arrays.asList(numbers));
    }

    private String name;

    private List<String> number;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getNumberList() {
        return number;
    }

    public void setNumber(List<String> number) {
        this.number = number;
    }

}

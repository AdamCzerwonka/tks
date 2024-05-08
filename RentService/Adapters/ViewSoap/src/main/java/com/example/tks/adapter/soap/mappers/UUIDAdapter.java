package com.example.tks.adapter.soap.mappers;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

import java.util.UUID;

public class UUIDAdapter extends XmlAdapter<String, UUID> {
    @Override
    public UUID unmarshal(String string) throws Exception {
        return UUID.fromString(string);
    }

    @Override
    public String marshal(UUID uuid) throws Exception {
        return uuid.toString();
    }
}

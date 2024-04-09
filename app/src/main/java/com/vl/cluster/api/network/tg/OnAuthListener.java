package com.vl.cluster.api.network.tg;

public interface OnAuthListener {
    Long getNextRequestAvailableAt();

    Integer requestCode();

    TelegramNetwork.TelegramSession signIn() throws InterruptedException;
}

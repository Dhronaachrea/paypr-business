package com.skilrock.retailapp.models.scratch;

import android.support.annotation.NonNull;

import java.util.Arrays;

public class SaleTicketRequestBean {

    private String gameType;

    private String soldChannel;

    private String[] ticketNumberList;

    private String userName;

    private String userSessionId;

    public String getGameType ()
    {
        return gameType;
    }

    public void setGameType (String gameType)
    {
        this.gameType = gameType;
    }

    public String getSoldChannel ()
    {
        return soldChannel;
    }

    public void setSoldChannel (String soldChannel)
    {
        this.soldChannel = soldChannel;
    }

    public String[] getTicketNumberList ()
    {
        return ticketNumberList;
    }

    public void setTicketNumberList (String[] ticketNumberList)
    {
        this.ticketNumberList = ticketNumberList;
    }

    public String getUserName ()
    {
        return userName;
    }

    public void setUserName (String userName)
    {
        this.userName = userName;
    }

    public String getUserSessionId ()
    {
        return userSessionId;
    }

    public void setUserSessionId (String userSessionId)
    {
        this.userSessionId = userSessionId;
    }

    @NonNull
    @Override
    public String toString()
    {
        return "SaleTicketRequestBean [gameType: " + gameType + ", soldChannel: " + soldChannel +
                ", ticketNumberList: " + Arrays.toString(ticketNumberList) + ", userName: " + userName + ", userSessionId: " + userSessionId + "]";
    }

}

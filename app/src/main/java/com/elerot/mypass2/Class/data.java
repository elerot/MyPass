package com.elerot.mypass2;

/**
 * Created by selcuk.celik on 23.12.2015.
 */
public class data {
    public boolean cbChecked;
    public int vsblty;
    public int _id;
    public long userID;
    public String displayName;
    public String userName;
    public String pass;
    public String description;

    public data(boolean cbChecked, int vsblty, int _id, long userID, String displayName, String userName, String pass, String description) {
        this.cbChecked = cbChecked;
        this.vsblty = vsblty;
        this._id = _id;
        this.userID = userID;
        this.displayName = displayName;
        this.userName = userName;
        this.pass = pass;
        this.description = description;
    }

}

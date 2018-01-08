package com.example.cassio.Graduation_Project.models;

/**
 * Created by cassio on 07/01/18.
 */

public class SavedEvent {
    public String savedfrom;


    public SavedEvent() {
    }

    public SavedEvent(String savedfrom) {
        this.savedfrom = savedfrom;
    }

    public String getSavedfrom() {
        return savedfrom;
    }

    public void setSavedfrom(String saved_from) {
        this.savedfrom = savedfrom;
    }
}

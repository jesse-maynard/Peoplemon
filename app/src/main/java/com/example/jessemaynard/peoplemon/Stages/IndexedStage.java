package com.example.jessemaynard.peoplemon.Stages;

import com.davidstemmer.screenplay.stage.XmlStage;

/**
 * Created by jessemaynard on 11/7/16.
 */

public abstract class IndexedStage extends XmlStage {

    public final String id;

    protected IndexedStage(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IndexedStage that = (IndexedStage) o;

        return !(id != null ? !id.equals(that.id) : that.id != null);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Przemek
 */
public class RecordIds {
    private long id;
    private long recordId;

    public RecordIds() {
    }

    public RecordIds(long id, long recordId) {
        this.id = id;
        this.recordId = recordId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getRecordId() {
        return recordId;
    }

    public void setRecordId(long recordId) {
        this.recordId = recordId;
    }

    @Override
    public String toString() {
        return "RecordIds{"
                + "id=" + id
                + ", recordId=" + recordId
                + '}';
    }
}

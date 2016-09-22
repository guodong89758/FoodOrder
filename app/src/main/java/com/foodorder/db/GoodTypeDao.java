package com.foodorder.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.foodorder.db.bean.GoodType;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "GOOD_TYPE".
*/
public class GoodTypeDao extends AbstractDao<GoodType, Long> {

    public static final String TABLENAME = "GOOD_TYPE";

    /**
     * Properties of entity GoodType.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Id_category = new Property(1, String.class, "id_category", false, "ID_CATEGORY");
        public final static Property Zh_name = new Property(2, String.class, "zh_name", false, "ZH_NAME");
        public final static Property Fr_name = new Property(3, String.class, "fr_name", false, "FR_NAME");
        public final static Property Position = new Property(4, int.class, "position", false, "POSITION");
        public final static Property Active = new Property(5, boolean.class, "active", false, "ACTIVE");
    }


    public GoodTypeDao(DaoConfig config) {
        super(config);
    }
    
    public GoodTypeDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"GOOD_TYPE\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"ID_CATEGORY\" TEXT," + // 1: id_category
                "\"ZH_NAME\" TEXT," + // 2: zh_name
                "\"FR_NAME\" TEXT," + // 3: fr_name
                "\"POSITION\" INTEGER NOT NULL ," + // 4: position
                "\"ACTIVE\" INTEGER NOT NULL );"); // 5: active
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"GOOD_TYPE\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, GoodType entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String id_category = entity.getId_category();
        if (id_category != null) {
            stmt.bindString(2, id_category);
        }
 
        String zh_name = entity.getZh_name();
        if (zh_name != null) {
            stmt.bindString(3, zh_name);
        }
 
        String fr_name = entity.getFr_name();
        if (fr_name != null) {
            stmt.bindString(4, fr_name);
        }
        stmt.bindLong(5, entity.getPosition());
        stmt.bindLong(6, entity.getActive() ? 1L: 0L);
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, GoodType entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String id_category = entity.getId_category();
        if (id_category != null) {
            stmt.bindString(2, id_category);
        }
 
        String zh_name = entity.getZh_name();
        if (zh_name != null) {
            stmt.bindString(3, zh_name);
        }
 
        String fr_name = entity.getFr_name();
        if (fr_name != null) {
            stmt.bindString(4, fr_name);
        }
        stmt.bindLong(5, entity.getPosition());
        stmt.bindLong(6, entity.getActive() ? 1L: 0L);
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public GoodType readEntity(Cursor cursor, int offset) {
        GoodType entity = new GoodType( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // id_category
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // zh_name
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // fr_name
            cursor.getInt(offset + 4), // position
            cursor.getShort(offset + 5) != 0 // active
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, GoodType entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setId_category(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setZh_name(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setFr_name(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setPosition(cursor.getInt(offset + 4));
        entity.setActive(cursor.getShort(offset + 5) != 0);
     }
    
    @Override
    protected final Long updateKeyAfterInsert(GoodType entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(GoodType entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(GoodType entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}

package com.foodorder.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.foodorder.db.bean.Good;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "GOOD".
*/
public class GoodDao extends AbstractDao<Good, Long> {

    public static final String TABLENAME = "GOOD";

    /**
     * Properties of entity Good.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, long.class, "id", true, "_id");
        public final static Property Search_num = new Property(1, String.class, "search_num", false, "SEARCH_NUM");
        public final static Property Id_product = new Property(2, String.class, "id_product", false, "ID_PRODUCT");
        public final static Property Reference = new Property(3, String.class, "reference", false, "REFERENCE");
        public final static Property Zh_name = new Property(4, String.class, "zh_name", false, "ZH_NAME");
        public final static Property Fr_name = new Property(5, String.class, "fr_name", false, "FR_NAME");
        public final static Property Quantity = new Property(6, int.class, "quantity", false, "QUANTITY");
        public final static Property Unit = new Property(7, String.class, "unit", false, "UNIT");
        public final static Property Price = new Property(8, double.class, "price", false, "PRICE");
        public final static Property Reducable = new Property(9, boolean.class, "reducable", false, "REDUCABLE");
        public final static Property Max_attributes_choose = new Property(10, int.class, "max_attributes_choose", false, "MAX_ATTRIBUTES_CHOOSE");
        public final static Property Image_url = new Property(11, String.class, "image_url", false, "IMAGE_URL");
    }

    private DaoSession daoSession;


    public GoodDao(DaoConfig config) {
        super(config);
    }
    
    public GoodDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"GOOD\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ," + // 0: id
                "\"SEARCH_NUM\" TEXT," + // 1: search_num
                "\"ID_PRODUCT\" TEXT," + // 2: id_product
                "\"REFERENCE\" TEXT," + // 3: reference
                "\"ZH_NAME\" TEXT," + // 4: zh_name
                "\"FR_NAME\" TEXT," + // 5: fr_name
                "\"QUANTITY\" INTEGER NOT NULL ," + // 6: quantity
                "\"UNIT\" TEXT," + // 7: unit
                "\"PRICE\" REAL NOT NULL ," + // 8: price
                "\"REDUCABLE\" INTEGER NOT NULL ," + // 9: reducable
                "\"MAX_ATTRIBUTES_CHOOSE\" INTEGER NOT NULL ," + // 10: max_attributes_choose
                "\"IMAGE_URL\" TEXT);"); // 11: image_url
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"GOOD\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Good entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
 
        String search_num = entity.getSearch_num();
        if (search_num != null) {
            stmt.bindString(2, search_num);
        }
 
        String id_product = entity.getId_product();
        if (id_product != null) {
            stmt.bindString(3, id_product);
        }
 
        String reference = entity.getReference();
        if (reference != null) {
            stmt.bindString(4, reference);
        }
 
        String zh_name = entity.getZh_name();
        if (zh_name != null) {
            stmt.bindString(5, zh_name);
        }
 
        String fr_name = entity.getFr_name();
        if (fr_name != null) {
            stmt.bindString(6, fr_name);
        }
        stmt.bindLong(7, entity.getQuantity());
 
        String unit = entity.getUnit();
        if (unit != null) {
            stmt.bindString(8, unit);
        }
        stmt.bindDouble(9, entity.getPrice());
        stmt.bindLong(10, entity.getReducable() ? 1L: 0L);
        stmt.bindLong(11, entity.getMax_attributes_choose());
 
        String image_url = entity.getImage_url();
        if (image_url != null) {
            stmt.bindString(12, image_url);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Good entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
 
        String search_num = entity.getSearch_num();
        if (search_num != null) {
            stmt.bindString(2, search_num);
        }
 
        String id_product = entity.getId_product();
        if (id_product != null) {
            stmt.bindString(3, id_product);
        }
 
        String reference = entity.getReference();
        if (reference != null) {
            stmt.bindString(4, reference);
        }
 
        String zh_name = entity.getZh_name();
        if (zh_name != null) {
            stmt.bindString(5, zh_name);
        }
 
        String fr_name = entity.getFr_name();
        if (fr_name != null) {
            stmt.bindString(6, fr_name);
        }
        stmt.bindLong(7, entity.getQuantity());
 
        String unit = entity.getUnit();
        if (unit != null) {
            stmt.bindString(8, unit);
        }
        stmt.bindDouble(9, entity.getPrice());
        stmt.bindLong(10, entity.getReducable() ? 1L: 0L);
        stmt.bindLong(11, entity.getMax_attributes_choose());
 
        String image_url = entity.getImage_url();
        if (image_url != null) {
            stmt.bindString(12, image_url);
        }
    }

    @Override
    protected final void attachEntity(Good entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.getLong(offset + 0);
    }    

    @Override
    public Good readEntity(Cursor cursor, int offset) {
        Good entity = new Good( //
            cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // search_num
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // id_product
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // reference
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // zh_name
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // fr_name
            cursor.getInt(offset + 6), // quantity
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // unit
            cursor.getDouble(offset + 8), // price
            cursor.getShort(offset + 9) != 0, // reducable
            cursor.getInt(offset + 10), // max_attributes_choose
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11) // image_url
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Good entity, int offset) {
        entity.setId(cursor.getLong(offset + 0));
        entity.setSearch_num(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setId_product(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setReference(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setZh_name(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setFr_name(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setQuantity(cursor.getInt(offset + 6));
        entity.setUnit(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setPrice(cursor.getDouble(offset + 8));
        entity.setReducable(cursor.getShort(offset + 9) != 0);
        entity.setMax_attributes_choose(cursor.getInt(offset + 10));
        entity.setImage_url(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Good entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Good entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Good entity) {
        throw new UnsupportedOperationException("Unsupported for entities with a non-null key");
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
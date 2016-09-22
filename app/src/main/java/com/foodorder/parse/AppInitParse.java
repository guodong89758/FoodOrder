package com.foodorder.parse;

import com.foodorder.db.bean.Attribute;
import com.foodorder.db.bean.Formula;
import com.foodorder.db.bean.Good;
import com.foodorder.db.bean.GoodType;
import com.foodorder.logic.UserManager;
import com.foodorder.runtime.RT;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by guodong on 16/9/20.
 */
public class AppInitParse {

    public static void parseJson(JSONObject json) {
        if (json == null) {
            return;
        }

        JSONObject restaurant = json.optJSONObject("Restaurant");
        if (restaurant == null) {
            return;
        }
        JSONObject users = restaurant.optJSONObject("Users");
        //用户信息
        if (users != null) {
            JSONArray userArray = users.optJSONArray("User");
            if (userArray != null && userArray.length() > 0) {
                UserManager.getInstance().getUserList().clear();
                for (int i = 0; i < userArray.length(); i++) {
                    JSONObject user = userArray.optJSONObject(i);
                    String id_user = user.optString("id_user");
                    String name = user.optString("name");
                    String login = user.optString("login");

                    UserManager.getInstance().addUsername(login);
                }
            }
        }
        //菜单信息
        JSONArray categoryArray = restaurant.optJSONArray("Category");
        if (categoryArray != null && categoryArray.length() > 0) {
            RT.ins().getDaoSession().getGoodTypeDao().deleteAll();
            RT.ins().getDaoSession().getGoodDao().deleteAll();
            RT.ins().getDaoSession().getAttributeDao().deleteAll();
            RT.ins().getDaoSession().getFormulaDao().deleteAll();
            for (int i = 0; i < categoryArray.length(); i++) {
                JSONObject category = categoryArray.optJSONObject(i);
                String id_category = category.optString("id_category");
                String fr_category_name = category.optString("name");
                int category_position = category.optInt("position", 0);
                JSONObject zh_category = category.optJSONObject("zh");
                String zh_category_name = "";
                if (zh_category != null) {
                    zh_category_name = zh_category.optString("name");
                }
                GoodType goodType = new GoodType();
                goodType.setId_category(id_category);
                goodType.setFr_name(fr_category_name);
                goodType.setPosition(category_position);
                goodType.setActive(category.optBoolean("active"));
                goodType.setZh_name(zh_category_name);
                //保存菜单分类数据到数据库
                RT.ins().getDaoSession().getGoodTypeDao().insert(goodType);
                JSONObject products = category.optJSONObject("Products");
                if (products != null) {
                    JSONArray productArray = products.optJSONArray("Product");
                    if (productArray != null && productArray.length() > 0) {
                        for (int j = 0; j < productArray.length(); j++) {
                            JSONObject product = productArray.optJSONObject(j);
                            String id_product = product.optString("id_product");
                            String reference = product.optString("reference");
                            String fr_product_name = product.optString("name");
                            int quantity = product.optInt("quantity", 0);
                            String unit = product.optString("unit");
                            double price = product.optDouble("price");
                            boolean reducable = product.optBoolean("reducable");
                            int max_attributes_choose = product.optInt("max_attributes_choose");
                            JSONObject product_zh = product.optJSONObject("zh");
                            String zh_product_name = "";
                            if (product_zh != null) {
                                zh_product_name = product_zh.optString("name");
                            }
                            String product_image_url = "";
                            JSONObject images = product.optJSONObject("Images");
                            if (images != null) {
                                JSONObject image = images.optJSONObject("Image");
                                if (image != null) {
                                    product_image_url = "/img/" + image.optString("id_image") + ".jpg";
                                }
                            }

                            Good good = new Good();
                            good.setId_category(id_category);
                            good.setFr_category_name(fr_category_name);
                            good.setZh_category_name(zh_category_name);
                            good.setPosition(category_position);
                            good.setId_product(id_product);
                            good.setReference(reference);
                            good.setFr_name(fr_product_name);
                            good.setZh_name(zh_product_name);
                            good.setQuantity(quantity);
                            good.setUnit(unit);
                            good.setPrice(price);
                            good.setReducable(reducable);
                            good.setMax_attributes_choose(max_attributes_choose);
                            good.setImage_url(product_image_url);
                            good.setIsFormula(product.isNull("Formulas") ? false : true);

                            //保存菜品信息到数据库
                            RT.ins().getDaoSession().getGoodDao().insert(good);

                            //规格信息
                            if (!product.isNull("Attributes")) {
                                JSONObject attributes = product.optJSONObject("Attributes");
                                if (attributes != null) {
                                    JSONArray attributeArray = attributes.optJSONArray("Attribute");
                                    if (attributeArray != null && attributeArray.length() > 0) {
                                        for (int k = 0; k < attributeArray.length(); k++) {
                                            JSONObject attribute = attributeArray.optJSONObject(k);
                                            String id_product_attribute = attribute.optString("id_product_attribute");
                                            String fr_attribute = attribute.optString("name");
                                            String value = attribute.optString("value");
                                            String zh_attribute = "";
                                            JSONObject attribute_zh = attribute.optJSONObject("zh");
                                            if (attribute_zh != null) {
                                                zh_attribute = attribute_zh.optString("name");
                                            }
                                            Attribute attr = new Attribute();
                                            attr.setId_product(id_product);
                                            attr.setId_product_attribute(id_product_attribute);
                                            attr.setZh_name(zh_attribute);
                                            attr.setFr_name(fr_attribute);
                                            attr.setValue(value);
                                            //保存菜品规格信息
                                            RT.ins().getDaoSession().getAttributeDao().insert(attr);
                                        }
                                    }
                                }
                            }
                            //套餐信息
                            if (!product.isNull("Formulas")) {
                                JSONObject formulas = product.optJSONObject("Formulas");
                                if (formulas != null) {
                                    JSONArray formulaArray = formulas.optJSONArray("Formula");
                                    if (formulaArray != null && formulaArray.length() > 0) {
                                        for (int m = 0; m < formulaArray.length(); m++) {
                                            JSONObject formula = formulaArray.optJSONObject(m);
                                            String id_product_formula = formula.optString("id_product_formula");
                                            String fr_formula_name = formula.optString("name");
                                            int max_choose = formula.optInt("max_choose", 1);
                                            String zh_formula_name = "";
                                            JSONObject formula_zh = formula.optJSONObject("zh");
                                            if (formula_zh != null) {
                                                zh_formula_name = formula_zh.optString("name");
                                            }
                                            //套餐下菜品信息
                                            if (!formula.isNull("FormulaItem")) {
                                                JSONArray formulaItemArray = formula.optJSONArray("FormulaItem");
                                                if (formulaItemArray != null && formulaItemArray.length() > 0) {
                                                    for (int n = 0; n < formulaItemArray.length(); n++) {
                                                        JSONObject formulaItem = formulaItemArray.optJSONObject(n);
                                                        String id_product_formula_item = formulaItem.optString("id_product_formula_item");
                                                        String id_product_item = formulaItem.optString("id_product_item");
                                                        int formula_item_position = formulaItem.optInt("position", 0);

                                                        Formula formulaBean = new Formula();
                                                        formulaBean.setId_product(id_product);
                                                        formulaBean.setId_product_formula(id_product_formula);
                                                        formulaBean.setZh_type_name(zh_formula_name);
                                                        formulaBean.setFr_type_name(fr_formula_name);
                                                        formulaBean.setMax_choose(max_choose);
                                                        formulaBean.setId_product_formula_item(id_product_formula_item);
                                                        formulaBean.setId_product_item(id_product_item);
                                                        formulaBean.setPosition(formula_item_position);

                                                        //保存套餐菜品信息
                                                        RT.ins().getDaoSession().getFormulaDao().insert(formulaBean);

                                                    }
                                                }
                                            }

                                        }
                                    }
                                }
                            }


                        }

                    }
                }


            }
        }

    }
}

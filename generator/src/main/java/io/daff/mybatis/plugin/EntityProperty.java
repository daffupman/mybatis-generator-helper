package io.daff.mybatis.plugin;

/**
 * Java实体类中的属性
 *
 * @author daffupman
 * @since 2020/12/6
 */
public class EntityProperty {

    /**
     * 属性名称
     */
    private String propertyName;

    /**
     * 属性类型
     */
    private String javaShortPropertyType;

    /**
     * 属性对应表中的列名
     */
    private String columnName;

    public EntityProperty() {}

    public EntityProperty(String propertyName, String javaShortPropertyType, String columnName) {
        this.propertyName = propertyName;
        this.javaShortPropertyType = javaShortPropertyType;
        this.columnName = columnName;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getJavaShortPropertyType() {
        return javaShortPropertyType;
    }

    public void setJavaShortPropertyType(String javaShortPropertyType) {
        this.javaShortPropertyType = javaShortPropertyType;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }
}

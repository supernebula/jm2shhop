package cn.evshop.dao;

import java.sql.SQLException;
import java.util.List;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.sun.org.apache.bcel.internal.generic.NEW;

import cn.evshop.domain.Product;
import cn.evshop.util.DataSourceUtils;

public class ProductDao {

	//添加商品
	public void addProduct(Product item) throws SQLException
	{
		String sql = "INSERT INTO product VALUES(?,?,?,?,?,?,?)";
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		runner.update(sql, item.getId(), item.getName(), item.getPrice(), item.getCategory(), item.getStockNum(), item.getImgUrl(), item.getDescription());
	}
	
	//查找所有商品
	public List<Product> listAll() throws SQLException{
		String sql = "SELECT * FROM product";
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		List<Product> list = runner.query(sql, new BeanListHandler<Product>(Product.class));
		return list;
	}
	
	// 获取所有商品
	public int findAllCount(String category) throws SQLException{
		String sql = "SELECT COUNT(*) FROM product";
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		if(!"全部商品".equals(category)) {
			sql += " WHERE category=?";
			Long count = (Long) runner.query(sql, new ScalarHandler<Long>(), category);
			return count.intValue();
		}else {
			Long count = (Long)runner.query(sql, new ScalarHandler<Long>());
			return count.intValue();
		}
	}
	
	// 获取当前页数据
	public List<Product> findByPage(int pageIndex, int pageSize, String category) throws SQLException{
		String sql = null;
		Object[] params  = null;
		if(!"全部商品".equals(category)) {
			sql = "SELECT * FORM product WHERE category = ? LIMIT ?,?";
			params = new Object[] {category, (pageIndex = 1) * pageSize, pageSize};
		}else {
			sql = "SELECT * FORM product LIMIT ?,?";
			params = new Object[] {(pageIndex = 1) * pageSize, pageSize};
		}
		
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		return runner.query(sql, new BeanListHandler<Product>(Product.class), params);
	}
}

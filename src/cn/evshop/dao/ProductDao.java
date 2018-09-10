package cn.evshop.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.mysql.cj.Query;
import com.sun.istack.internal.Nullable;
import com.sun.mail.imap.protocol.Item;
import com.sun.org.apache.bcel.internal.generic.NEW;

import cn.evshop.domain.HostSaleProduct;
import cn.evshop.domain.Order;
import cn.evshop.domain.OrderItem;
import cn.evshop.domain.Product;
import cn.evshop.domain.SaleRankItem;
import cn.evshop.util.DataSourceUtils;
import jdk.internal.org.objectweb.asm.tree.analysis.Value;
import sun.management.counter.Variability;

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
	
	//根据id查询商品
	public Product findProductById(String id) throws SQLException{
		String sql = "SELECT * FROM product WHERE id = ?";
		QueryRunner runner = new QueryRunner();
		Product item = runner.query(sql, new BeanHandler<Product>(Product.class), id);
		return item;
	}
	
	// 生成订单时，将商品数量减少
	public void changeProductNumer(Order order) throws SQLException{
		
		
	}
	
	//销售榜单
	public List<SaleRankItem> salesList(int year, int month) throws SQLException {
		String sql = "SELECT products.name,SUM(orderitem.buynum) totalsalnum FROM orders,products,orderitem WHERE orders.id= orderitem.order_id AND products.id= orderitem.product_id AND orders.paystate=1 and year(ordertime)=? and month(ordertime)=? GROUP BY products.name ORDER BY totalsalnum DESC";
		QueryRunner runner = new QueryRunner();
		List<SaleRankItem> list = runner.query(sql, new BeanListHandler<SaleRankItem>(SaleRankItem.class), year, month);
		return list;
	}
	
	//多条件查询
	public List<Product> FindProducts(String id, String name, String category, @Nullable Double minPrice, Double maxPrice) throws SQLException{
		String sql = "SELECT * FROM product";
		
		String condition = null;
		
		List<Object> params = new ArrayList<Object>();
		List<String> conditions = new ArrayList<String>();
		if(id != null && id.trim() != "") {
			conditions.add("id = ?");
			params.add(id);
		}
		if(name != null && name.trim() != "") {
			conditions.add("name = ?");
			params.add(name);
		}
		
		if(category != null && category.trim() != "") {
			conditions.add("category = ?");
			params.add(category);
		}
		
		if(minPrice != null && maxPrice != null) {
			conditions.add("price BETWEEN ? AND ?");
			params.add(minPrice);
			params.add(maxPrice);
		}else if(minPrice != null) {
			conditions.add("price > ?");
			params.add(minPrice);
		}else {
			conditions.add("price < ?");
			params.add(maxPrice);
		}
		
		String conditStr = null;
		conditStr = String.join(" AND ", conditions);
		if(conditStr != null && conditStr.trim() != "")
		{
			sql += "WHERE " + conditStr;
		}
		
		QueryRunner runner = new QueryRunner();
		List<Product> items = runner.query(sql, new BeanListHandler<Product>(Product.class), params);
		return items;
	}
	
	// 修改商品信息
	public void editProduct(Product item) throws SQLException{
		
	}
	
	//删除订单时，修改商品数量
	public void updateProductNum(List<OrderItem> items) throws SQLException{
		
	}
	
	// 前台，获取本周热销商品
	public List<HostSaleProduct> getWeekHotProduct() throws SQLException{
		return null;
	}	
	
	//前台，用于搜索框根据书名来模糊查询相应的图书
		public List<Product> findBookByName(int currentPage, int currentCount,
				String searchfield) throws SQLException {
			//根据名字模糊查询图书
			String sql = "SELECT * FROM products WHERE name LIKE '%"+searchfield+"%' LIMIT ?,?";
			QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
//			//用于分页查询的数据
//			Object obj = new Object[] { (currentPage - 1) * currentCount, currentCount };
			return runner.query(sql, 
					new BeanListHandler<Product>(Product.class),currentPage-1,currentCount);
		}

		//前台搜索框，根据书名模糊查询出的图书总数量
		public int findBookByNameAllCount(String searchfield) throws SQLException {
			String sql = "SELECT COUNT(*) FROM products WHERE name LIKE '%"+searchfield+"%'";
			QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
			//查询出满足条件的总数量，为long类型
			Long count = (Long)runner.query(sql, new ScalarHandler());
			return count.intValue();
		}

		//后台系统，根据id删除商品信息
		public void deleteProduct(String id) throws SQLException {
			String sql = "DELETE FROM products WHERE id = ?";
			QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
			runner.update(sql, id);
	
	
	
	
}

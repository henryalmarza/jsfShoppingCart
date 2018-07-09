package com.pruebas.dao;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.pruebas.db.ProductoEntityManager;

public class DatabaseOperations {

	private static final String PERSISTENCE_UNIT_NAME = "JsfShoppingCart";	
	private static EntityManager entityMgrObj = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME).createEntityManager();
	private static EntityTransaction transactionObj = entityMgrObj.getTransaction();

	@SuppressWarnings("unchecked")
	public static List obtenerProductos() {
		Query queryObj = entityMgrObj.createQuery("SELECT s FROM ProductoEntityManager s");
		List productoList = queryObj.getResultList();
		if (productoList != null && productoList.size() > 0) {			
			return productoList;
		} else {
			return null;
		}
	}

	public static String crearNuevoProducto(String nombre, String descripcion, String categoria, Double precio) {
		if(!transactionObj.isActive()) {
			transactionObj.begin();
		}

		ProductoEntityManager newProductoObj = new ProductoEntityManager();
		newProductoObj.setIdProducto(obtenerIdProductoMaximo());
		newProductoObj.setNombre(nombre);
		newProductoObj.setDescripcion(descripcion);
		newProductoObj.setIdCategoria(categoria);
		newProductoObj.setPrecio(precio);
		entityMgrObj.persist(newProductoObj);
		transactionObj.commit();
		return "listadoProductos.xhtml?faces-redirect=true";	
	}

	// Method To Delete The Selected School Id From The Database 
	public static String removerProducto(int id) {
		if (!transactionObj.isActive()) {
			transactionObj.begin();
		}

		ProductoEntityManager deleteProductoObj = new ProductoEntityManager();
		if(verificarIdProducto(id)) {
			deleteProductoObj.setIdProducto(id);
			entityMgrObj.remove(entityMgrObj.merge(deleteProductoObj));
		}		
		transactionObj.commit();
		return "schoolsList.xhtml?faces-redirect=true";
	}

	// Method To Update The School Details For A Particular School Id In The Database
	public static String guardarCambiosProducto(int idProducto, String nombre, String descripcion, String idCategoria, Double precio) {
		if (!transactionObj.isActive()) {
			transactionObj.begin();
		}

		if(verificarIdProducto(idProducto)) {
			Query queryObj = entityMgrObj.createQuery("UPDATE ProductoEntityManager s SET s.nombre=:nombre, s.descripcion=:descripcion, s.idCategoria=:idCategoria, s.precio=:precio WHERE s.idProducto= :idProducto");			
			queryObj.setParameter("idProducto", idProducto);
			queryObj.setParameter("nombre", nombre);
			queryObj.setParameter("descripcion", descripcion);
			queryObj.setParameter("idCategoria", idCategoria);
			queryObj.setParameter("precio", precio);
			int updateCount = queryObj.executeUpdate();
			if(updateCount > 0) {
				System.out.println("El producto con Id: " + idProducto + " ha sido actualizado");
			}
		}
		transactionObj.commit();
		FacesContext.getCurrentInstance().addMessage("formularioEditarProducto:idProducto", new FacesMessage("EL producto #" + idProducto + " ha sido actualizado"));
		return "editarProducto.xhtml";
	}

	private static int obtenerIdProductoMaximo() {
		int idMax = 1;
		Query queryObj = entityMgrObj.createQuery("SELECT MAX(s.id)+1 FROM ProductoEntityManager s");
		if(queryObj.getSingleResult() != null) {
			idMax = (Integer) queryObj.getSingleResult();
		}
		return idMax;
	}

	private static boolean verificarIdProducto(int idProducto) {
		boolean idResult = false;
		Query queryObj = entityMgrObj.createQuery("SELECT s FROM ProductoEntityManager s WHERE s.id = :id");
		queryObj.setParameter("idProducto", idProducto);
		ProductoEntityManager idProductoSeleccionado = (ProductoEntityManager) queryObj.getSingleResult();
		if(idProductoSeleccionado != null) {
			idResult = true;
		}
		return idResult;
	}
}

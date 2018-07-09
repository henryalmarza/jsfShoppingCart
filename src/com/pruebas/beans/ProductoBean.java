package com.pruebas.beans;

import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import com.pruebas.dao.DatabaseOperations;

@ManagedBean
public class ProductoBean {
	private int idProducto;
	private String nombre;
	private String descripcion;
	private String idCategoria;
	private Double precio;
	private String codigoEditar;

	
	public int getIdProducto() {
		return idProducto;
	}

	public void setIdProducto(int idProducto) {
		this.idProducto = idProducto;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getIdCategoria() {
		return idCategoria;
	}

	public void setIdCategoria(String idCategoria) {
		this.idCategoria = idCategoria;
	}

	public Double getPrecio() {
		return precio;
	}

	public void setPrecio(Double precio) {
		this.precio = precio;
	}

	public String getCodigoEditar() {
		return codigoEditar;
	}

	public void setCodigoEditar(String codigoEditar) {
		this.codigoEditar = codigoEditar;
	}

	public List listarProductos() {
		return DatabaseOperations.obtenerProductos();
	}

	public String agregarNuevoProducto(ProductoBean productoBean) {
		return DatabaseOperations.crearNuevoProducto(productoBean.getNombre(), productoBean.getDescripcion(), productoBean.getIdCategoria(), productoBean.getPrecio());
	}

	public String eliminarProducto(int id) {
		return DatabaseOperations.removerProducto(id);
	}

	public String editarProducto() {
		codigoEditar = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
				.get("idProductoSeleccionado");
		return "editarProducto.xhtml";
	}

	public String actualizarProducto(ProductoBean productoBean) {
		return DatabaseOperations.guardarCambiosProducto(Integer.parseInt(productoBean.getCodigoEditar()),
				productoBean.getNombre(),productoBean.getDescripcion(),productoBean.getIdCategoria(),productoBean.getPrecio());
	}
}

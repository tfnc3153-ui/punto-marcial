package com.academia.puntomarcial.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.academia.puntomarcial.model.Producto;
import com.academia.puntomarcial.repository.ProductoRepository;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }

    public Producto buscarPorId(Integer id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
    }

    public Producto registrarProducto(Producto producto) {
        normalizar(producto);
        validarProducto(producto);

        if (productoRepository.existsByNombreIgnoreCaseAndTallaIgnoreCase(
                producto.getNombre(), producto.getTalla())) {
            throw new IllegalArgumentException("Ya existe un producto con ese nombre y talla");
        }

        if (producto.getEstado() == null || producto.getEstado().isBlank()) {
            producto.setEstado("ACTIVO");
        }

        return productoRepository.save(producto);
    }

    public Producto actualizarProducto(Integer id, Producto datos) {
        Producto producto = buscarPorId(id);

        normalizar(datos);
        validarProducto(datos);

        if (productoRepository.existsByNombreIgnoreCaseAndTallaIgnoreCaseAndIdProductoNot(
                datos.getNombre(), datos.getTalla(), id)) {
            throw new IllegalArgumentException("Ya existe otro producto con ese nombre y talla");
        }

        producto.setNombre(datos.getNombre());
        producto.setCategoria(datos.getCategoria());
        producto.setDisciplina(datos.getDisciplina());
        producto.setTalla(datos.getTalla());
        producto.setPrecio(datos.getPrecio());
        producto.setStock(datos.getStock());
        producto.setStockMinimo(datos.getStockMinimo());

        if (datos.getEstado() != null && !datos.getEstado().isBlank()) {
            producto.setEstado(datos.getEstado());
        }

        return productoRepository.save(producto);
    }

    private void normalizar(Producto producto) {
        if (producto.getNombre() != null) {
            producto.setNombre(producto.getNombre().trim());
        }
        if (producto.getTalla() == null || producto.getTalla().isBlank()) {
            producto.setTalla("Única");
        } else {
            producto.setTalla(producto.getTalla().trim());
        }
    }

    private void validarProducto(Producto producto) {
        if (!ReglasVenta.nombreValido(producto.getNombre())) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio");
        }
        if (!ReglasVenta.categoriaValida(producto.getCategoria())) {
            throw new IllegalArgumentException("La categoría no es válida");
        }
        if (!ReglasVenta.disciplinaValida(producto.getDisciplina())) {
            throw new IllegalArgumentException("La disciplina debe ser MMA, Muay Thai, Box o General");
        }
        if (!ReglasVenta.precioValido(producto.getPrecio())) {
            throw new IllegalArgumentException("El precio debe ser mayor a 0");
        }
        if (!ReglasVenta.stockValido(producto.getStock())) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
        if (!ReglasVenta.stockMinimoValido(producto.getStockMinimo())) {
            throw new IllegalArgumentException("El stock mínimo no puede ser negativo");
        }
    }
}

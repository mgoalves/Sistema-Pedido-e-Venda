package br.inf.ufg.pedidovenda.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.inf.ufg.pedidovenda.model.produto.Produto;
import br.inf.ufg.pedidovenda.service.NegocioException;
import br.inf.ufg.pedidovenda.util.jpa.Transactional;

public class Produtos implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public Produto guardar(Produto produto) {
		return manager.merge(produto);
	}

	public Produto porSku(String sku) {
		try {
			return manager.createQuery("from Produto where upper(sku) = :sku", Produto.class)
					.setParameter("sku", sku.toUpperCase()).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Produto> filtrados(ProdutoFilter filtro) {

		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(Produto.class);

		if (StringUtils.isNotBlank(filtro.getSku())) {

			criteria.add(Restrictions.eq("sku", filtro.getSku()));
		}

		if (StringUtils.isNotBlank(filtro.getNome())) {

			criteria.add(Restrictions.like("nome", filtro.getNome(), MatchMode.ANYWHERE));
		}

		return criteria.addOrder(Order.asc("nome")).list();

	}

	public Produto buscarPorId(Long id) {

		return manager.find(Produto.class, id);
	}

	/*
	 * Função que remove por meio de parametros um objeto do tipo produto do
	 * banco de dados.
	 */
	@Transactional
	public void remover(Produto produto) {
		try {
			produto = buscarPorId(produto.getId());
			manager.remove(produto);
			manager.flush();
		} catch (PersistenceException e) {
			throw new NegocioException("Produto não pode ser excluído.");
		}
	}

	public List<Produto> buscarPorNome(String nome) {

		return this.manager.createQuery("from Produto where upper(nome) like :nome", Produto.class)
				.setParameter("nome", "%" + nome.toUpperCase() + "%").getResultList();
	}

}
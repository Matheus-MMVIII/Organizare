function ClothingList({ clothing, loading, deletingId, updatingStockId, onDelete, updateClothingStock }) {
  return (
    <div className="list-grid">
      {loading ? <p className="empty-state">Carregando roupas...</p> : null}
      {!loading && clothing.length === 0 ? <p className="empty-state">Nenhuma roupa encontrada.</p> : null}

      {clothing.map((item) => (
        <article key={item.id} className="entity-card clothing-card">
          <div>
            <h3>{item.name}</h3>
            <p>
              {item.color} • Tam. {item.size}
            </p>
          </div>

          <dl>
            <div>
              <dt>Preco</dt>
              <dd>
                {new Intl.NumberFormat('pt-BR', {
                  style: 'currency',
                  currency: 'BRL',
                }).format(Number(item.price))}
              </dd>
            </div>
            <div>
              <dt>Estoque</dt>
              <div className="stock-control">
                <dd className={Number(item.stock) <= 5 ? 'highlight-stock' : ''}>{item.stock}</dd>
                <div className="stock-actions">
                  <button
                    className="stock-button stock-button-add"
                    type="button"
                    onClick={() => void updateClothingStock(item, Number(item.stock) + 1)}
                    disabled={updatingStockId === `clothing-${item.id}`}
                    aria-label={`Aumentar estoque de ${item.name}`}
                  >
                    +
                  </button>
                  <button
                    className="stock-button stock-button-remove"
                    type="button"
                    onClick={() => void updateClothingStock(item, Number(item.stock) - 1)}
                    disabled={updatingStockId === `clothing-${item.id}` || Number(item.stock) <= 0}
                    aria-label={`Diminuir estoque de ${item.name}`}
                  >
                    -
                  </button>
                </div>
              </div>
            </div>
          </dl>

          {Number(item.stock) <= 5 ? <span className="status-badge warm-badge">Estoque baixo</span> : null}

          <button
            className="danger-button"
            type="button"
            onClick={() => void onDelete(item.id)}
            disabled={deletingId === `clothing-${item.id}`}
          >
            {deletingId === `clothing-${item.id}` ? 'Removendo...' : 'Deletar roupa'}
          </button>
        </article>
      ))}
    </div>
  );
}

export default ClothingList;

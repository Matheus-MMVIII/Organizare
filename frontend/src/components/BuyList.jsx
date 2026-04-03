const currencyFormatter = new Intl.NumberFormat('pt-BR', {
  style: 'currency',
  currency: 'BRL',
});

const dateFormatter = new Intl.DateTimeFormat('pt-BR', {
  dateStyle: 'short',
  timeStyle: 'short',
});

function formatOrderDate(value) {
  const date = new Date(value ?? '');
  if (Number.isNaN(date.getTime())) {
    return 'Data indisponivel';
  }

  return dateFormatter.format(date);
}

function BuyList({ buys, loading, deletingId, onDelete }) {
  return (
    <div className="list-grid">
      {loading ? <p className="empty-state">Carregando compras...</p> : null}
      {!loading && buys.length === 0 ? <p className="empty-state">Nenhuma compra encontrada.</p> : null}

      {buys.map((buy) => (
        <article key={buy.id} className="entity-card buy-card">
          <div>
            <h3>{buy.clothingName || `Roupa #${buy.clothingId}`}</h3>
            <p>{buy.userName || `Usuario #${buy.userId}`}</p>
          </div>

          <dl>
            <div>
              <dt>Quantidade</dt>
              <dd>{buy.quantity}</dd>
            </div>
            <div>
              <dt>Total</dt>
              <dd>{currencyFormatter.format(Number(buy.totalPrice))}</dd>
            </div>
            <div>
              <dt>Data</dt>
              <dd>{formatOrderDate(buy.orderDate)}</dd>
            </div>
            <div>
              <dt>Pedido</dt>
              <dd>#{buy.id}</dd>
            </div>
          </dl>

          <span className="status-badge">Estoque sincronizado</span>

          <button
            className="danger-button"
            type="button"
            onClick={() => void onDelete(buy.id)}
            disabled={deletingId === `buy-${buy.id}`}
          >
            {deletingId === `buy-${buy.id}` ? 'Removendo...' : 'Deletar compra'}
          </button>
        </article>
      ))}
    </div>
  );
}

export default BuyList;

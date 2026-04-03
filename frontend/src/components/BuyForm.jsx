const currencyFormatter = new Intl.NumberFormat('pt-BR', {
  style: 'currency',
  currency: 'BRL',
});

function BuyForm({ form, users, clothing, onChange, onSubmit, submitting }) {
  const selectedClothing = clothing.find((item) => item.id === Number(form.clothingId));
  const quantity = Math.max(1, Number(form.quantity || 1));
  const estimatedTotal = selectedClothing ? Number(selectedClothing.price) * quantity : 0;
  const noUsers = users.length === 0;
  const availableClothing = clothing.filter((item) => Number(item.stock) > 0);
  const noClothing = availableClothing.length === 0;
  const disableSubmit = submitting || noUsers || noClothing;

  return (
    <article className="card section-card form-card buy-form-card">
      <div className="section-header compact-header">
        <div>
          <span className="eyebrow">Pedidos</span>
          <h3>Nova compra</h3>
        </div>
        <p>Escolha o usuario, a roupa e a quantidade. O total sai direto do backend.</p>
      </div>

      <form className="form-grid" onSubmit={onSubmit}>
        <label>
          Usuario
          <select
            className="clothing-form-select"
            name="userId"
            value={form.userId}
            onChange={onChange}
            disabled={noUsers}
            required
          >
            <option value="">{noUsers ? 'Cadastre um usuario primeiro' : 'Selecione um usuario'}</option>
            {users.map((user) => (
              <option key={user.id} value={user.id}>
                {user.name}
              </option>
            ))}
          </select>
        </label>

        <label>
          Roupa
          <select
            className="clothing-form-select"
            name="clothingId"
            value={form.clothingId}
            onChange={onChange}
            disabled={noClothing}
            required
          >
            <option value="">{noClothing ? 'Sem roupa com estoque' : 'Selecione uma roupa'}</option>
            {availableClothing.map((item) => (
              <option key={item.id} value={item.id}>
                {item.name} • estoque {item.stock}
              </option>
            ))}
          </select>
        </label>

        <label>
          Quantidade
          <input
            name="quantity"
            type="number"
            min="1"
            max={selectedClothing ? Number(selectedClothing.stock) : undefined}
            value={form.quantity}
            onChange={onChange}
            required
          />
        </label>

        <div className="buy-summary">
          <span className="eyebrow">Resumo</span>
          {selectedClothing ? (
            <>
              <strong>{currencyFormatter.format(estimatedTotal)}</strong>
              <p>
                {selectedClothing.name} • {quantity} unidade(s) • estoque atual {selectedClothing.stock}
              </p>
            </>
          ) : (
            <>
              <strong>Selecione uma roupa</strong>
              <p>Assim que voce escolher um item, o valor estimado aparece aqui.</p>
            </>
          )}
        </div>

        <button className="primary-button alt-button" type="submit" disabled={disableSubmit}>
          {submitting ? 'Salvando...' : 'Registrar compra'}
        </button>
      </form>
    </article>
  );
}

export default BuyForm;

function ClothingForm({ form, onChange, onSubmit, submitting }) {
  return (
    <article className="card section-card form-card">
      <div className="section-header compact-header">
        <div>
          <span className="eyebrow">Catalogo</span>
          <h3>Nova roupa</h3>
        </div>
        <p>Cadastre nome, preco, estoque, cor e tamanho.</p>
      </div>

      <form className="form-grid" onSubmit={onSubmit}>
        <label>
          Nome
          <input name="name" value={form.name} onChange={onChange} required />
        </label>
        <label>
          Preco
          <input
            name="price"
            type="number"
            min="0"
            step="0.01"
            value={form.price}
            onChange={onChange}
            required
          />
        </label>
        <label>
          Estoque
          <input name="stock" type="number" min="0" value={form.stock} onChange={onChange} required />
        </label>
        <label>
          Tamanho
          <input name="size" value={form.size} onChange={onChange} required />
        </label>
        <label>
          Cor
          <input name="color" value={form.color} onChange={onChange} required />
        </label>
        <button className="primary-button alt-button" type="submit" disabled={submitting}>
          {submitting ? 'Salvando...' : 'Adicionar roupa'}
        </button>
      </form>
    </article>
  );
}

export default ClothingForm;

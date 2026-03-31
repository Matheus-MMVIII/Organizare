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
          <select
            name="size"
            value={form.size || "PP"}
            onChange={onChange}
            required
          >
            <option value="PP">PP</option>
            <option value="P">P</option>
            <option value="M">M</option>
            <option value="G">G</option>
            <option value="GG">GG</option>
          </select>
        </label>
        <label>
          Cor
          <select
            name="color"
            value={form.color || "Branco"}
            onChange={onChange}
            required
          >
            <option value="Branco">Branco</option>
            <option value="Preto">Preto</option>
            <option value="Cinza">Cinza</option>
            <option value="Azul">Azul</option>
            <option value="Azul_marinho">Azul marinho</option>
            <option value="Vermelho">Vermelho</option>
            <option value="Verde">Verde</option>
            <option value="Verde_oliva">Verde oliva</option>
            <option value="Amarelo">Amarelo</option>
            <option value="Bege">Bege</option>
            <option value="Marrom">Marrom</option>
            <option value="Caramelo">Caramelo</option>
            <option value="Rosa">Rosa</option>
            <option value="Rosa_claro">Rosa claro</option>
            <option value="Roxo">Roxo</option>
            <option value="Lilas">Lilás</option>
            <option value="Laranja">Laranja</option>
            <option value="Vinho">Vinho (bordô)</option>
            <option value="Turquesa">Turquesa</option>
            <option value="Ciano">Ciano</option>
            <option value="Off_white">Off-white</option>
            <option value="Nude">Nude</option>
            <option value="Creme">Creme</option>
            <option value="Dourado">Dourado</option>
            <option value="Prateado">Prateado</option>
          </select>
        </label>
        <button className="primary-button alt-button" type="submit" disabled={submitting}>
          {submitting ? 'Salvando...' : 'Adicionar roupa'}
        </button>
      </form>
    </article>
  );
}

export default ClothingForm;

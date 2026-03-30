function UserForm({ form, onChange, onSubmit, submitting }) {
  return (
    <article className="card section-card form-card">
      <div className="section-header compact-header">
        <div>
          <span className="eyebrow">Cadastro</span>
          <h3>Novo usuario</h3>
        </div>
        <p>Adicione um usuario com os dados principais.</p>
      </div>

      <form className="form-grid" onSubmit={onSubmit}>
        <label>
          Nome
          <input name="name" value={form.name} onChange={onChange} required />
        </label>
        <label>
          Email
          <input name="email" type="email" value={form.email} onChange={onChange} required />
        </label>
        <label>
          Celular
          <input name="cellPhone" value={form.cellPhone} onChange={onChange} required />
        </label>
        <label>
          Data de aniversario
          <input
            name="birthday"
            type="date"
            value={form.birthday}
            onChange={onChange}
            required
          />
        </label>
        <button className="primary-button" type="submit" disabled={submitting}>
          {submitting ? 'Salvando...' : 'Adicionar usuario'}
        </button>
      </form>
    </article>
  );
}

export default UserForm;

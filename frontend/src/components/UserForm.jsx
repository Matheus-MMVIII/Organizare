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
          Mes aniversario
          <input
            name="birthMonth"
            type="number"
            min="1"
            max="12"
            value={form.birthMonth}
            onChange={onChange}
            required
          />
        </label>
        <label>
          Dia aniversario
          <input
            name="birthDay"
            type="number"
            min="1"
            max="31"
            value={form.birthDay}
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

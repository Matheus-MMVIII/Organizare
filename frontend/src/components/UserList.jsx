function UserList({ users, loading, deletingId, onDelete }) {
  function formatBirthday(user) {
    if (user.birthday) {
      const [, month, day] = user.birthday.split('-');
      if (month && day) {
        return `${day}/${month}`;
      }
    }

    return `${String(user.birthDay).padStart(2, '0')}/${String(user.birthMonth).padStart(2, '0')}`;
  }

  return (
    <div className="list-grid">
      {loading ? <p className="empty-state">Carregando usuarios...</p> : null}
      {!loading && users.length === 0 ? <p className="empty-state">Nenhum usuario encontrado.</p> : null}

      {users.map((user) => (
        <article key={user.id} className="entity-card user-card">
          <div>
            <h3>{user.name}</h3>
            <p>{user.email}</p>
          </div>

          <dl>
            <div>
              <dt>Celular</dt>
              <dd>{user.cellPhone}</dd>
            </div>
            <div>
              <dt>Aniversario</dt>
              <dd>{formatBirthday(user)}</dd>
            </div>
          </dl>

          {user.birthdayToday ? <span className="status-badge">Aniversario hoje</span> : null}

          <button
            className="danger-button"
            type="button"
            onClick={() => void onDelete(user.id)}
            disabled={deletingId === `user-${user.id}`}
          >
            {deletingId === `user-${user.id}` ? 'Removendo...' : 'Deletar usuario'}
          </button>
        </article>
      ))}
    </div>
  );
}

export default UserList;

import SectionIntro from '../components/SectionIntro';
import UserForm from '../components/UserForm';
import UserList from '../components/UserList';

function UsersPage({ app }) {
  return (
    <section className="page-content">
      <section className="page-header card">
        <SectionIntro
          eyebrow="Usuarios"
          title="Gerenciar usuarios"
          description="Aqui voce cadastra novos usuarios e tambem pode deletar qualquer cadastro existente."
          actions={
            <label className="search-box compact-search" htmlFor="user-search">
              <span>Buscar usuario</span>
              <input
                id="user-search"
                type="text"
                placeholder="Nome, email ou celular"
                value={app.userSearch}
                onChange={(event) => app.setUserSearch(event.target.value)}
              />
            </label>
          }
        />
      </section>

      <section className="workspace-grid page-grid">
        <UserForm
          form={app.userForm}
          onChange={app.updateUserField}
          onSubmit={app.createUser}
          submitting={app.submitting.user}
        />

        <article className="card section-card list-card">
          <div className="section-header compact-header sticky-header">
            <div>
              <span className="eyebrow">Lista</span>
              <h3>Usuarios cadastrados</h3>
            </div>
            <p>{app.filteredUsers.length} resultado(s)</p>
          </div>

          <UserList
            users={app.filteredUsers}
            loading={app.loading}
            deletingId={app.deletingId}
            onDelete={app.deleteUser}
          />
        </article>
      </section>
    </section>
  );
}

export default UsersPage;

import SectionIntro from '../components/SectionIntro';
import StatCard from '../components/StatCard';

function DashboardPage({ app, navigate }) {
  return (
    <section className="page-content">
      <section className="hero card page-hero">
        <div className="hero-copy">
          <span className="eyebrow">Painel principal</span>
          <h2>Veja o panorama e entre nas paginas certas com um clique.</h2>
          <p>
            Agora o sistema esta separado em paginas para facilitar o gerenciamento de usuarios e
            roupas sem misturar tudo em uma tela so.
          </p>
        </div>

        <div className="hero-actions quick-links">
          <button className="primary-button" type="button" onClick={() => navigate('users')}>
            Ir para usuarios
          </button>
          <button className="primary-button alt-button" type="button" onClick={() => navigate('clothing')}>
            Ir para roupas
          </button>
        </div>
      </section>

      <section className="stats-grid">
        <StatCard label="Usuarios cadastrados" value={app.users.length} detail="Total salvo na base" />
        <StatCard label="Roupas cadastradas" value={app.clothing.length} detail="Itens no catalogo" tone="accent-card" />
        <StatCard label="Estoque baixo" value={app.lowStockCount} detail="Itens com 5 ou menos" tone="warm-card" />
      </section>

      <section className="workspace-grid dashboard-grid">
        <article className="card section-card">
          <SectionIntro
            eyebrow="Usuarios"
            title="Ultimos usuarios"
            description="Uma visao rapida antes de abrir a pagina completa de gerenciamento."
          />

          <div className="mini-list">
            {app.loading ? <p className="empty-state">Carregando usuarios...</p> : null}
            {!app.loading && app.recentUsers.length === 0 ? <p className="empty-state">Nenhum usuario cadastrado.</p> : null}
            {app.recentUsers.map((user) => (
              <article key={user.id} className="mini-card">
                <strong>{user.name}</strong>
                <span>{user.email}</span>
              </article>
            ))}
          </div>
        </article>

        <article className="card section-card">
          <SectionIntro
            eyebrow="Roupas"
            title="Ultimos produtos"
            description="Acompanhe rapidamente as ultimas pecas adicionadas ao catalogo."
          />

          <div className="mini-list">
            {app.loading ? <p className="empty-state">Carregando roupas...</p> : null}
            {!app.loading && app.recentClothing.length === 0 ? <p className="empty-state">Nenhuma roupa cadastrada.</p> : null}
            {app.recentClothing.map((item) => (
              <article key={item.id} className="mini-card">
                <strong>{item.name}</strong>
                <span>
                  {item.color} • Tam. {item.size} • Estoque {item.stock}
                </span>
              </article>
            ))}
          </div>
        </article>
      </section>
    </section>
  );
}

export default DashboardPage;

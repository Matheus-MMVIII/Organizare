import FeedbackBanner from './FeedbackBanner';
import SidebarNav from './SidebarNav';

function AppLayout({ children, currentRoute, feedback, navigate, onRefresh, onClearFeedback }) {
  return (
    <div className="page-shell">
      <div className="ambient ambient-left" />
      <div className="ambient ambient-right" />

      <div className="app-shell">
        <SidebarNav currentRoute={currentRoute} navigate={navigate} />

        <main className="app-main">
          <header className="topbar card">
            <div>
              <span className="eyebrow">Organizare</span>
              <h1>Painel de organizacao</h1>
            </div>

            <button className="secondary-button" type="button" onClick={() => void onRefresh()}>
              Atualizar dados
            </button>
          </header>

          {feedback.message ? (
            <FeedbackBanner feedback={feedback} onClear={onClearFeedback} />
          ) : null}

          {children}
        </main>
      </div>
    </div>
  );
}

export default AppLayout;

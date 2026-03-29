import AppLayout from './components/AppLayout';
import useHashRoute from './hooks/useHashRoute';
import useOrganizareData from './hooks/useOrganizareData';
import ClothingPage from './pages/ClothingPage';
import DashboardPage from './pages/DashboardPage';
import UsersPage from './pages/UsersPage';

function App() {
  const [route, navigate] = useHashRoute();
  const app = useOrganizareData();

  let page = null;

  if (route === 'users') {
    page = <UsersPage app={app} />;
  } else if (route === 'clothing') {
    page = <ClothingPage app={app} />;
  } else {
    page = <DashboardPage app={app} navigate={navigate} />;
  }

  return (
    <AppLayout
      currentRoute={route}
      feedback={app.feedback}
      navigate={navigate}
      onRefresh={app.loadDashboard}
      onClearFeedback={app.clearFeedback}
    >
      {page}
    </AppLayout>
  );
}

export default App;

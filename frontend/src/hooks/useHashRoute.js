import { useEffect, useState } from 'react';

const allowedRoutes = new Set(['dashboard', 'users', 'clothing', 'buy']);

function normalizeHash(hash) {
  const route = hash.replace(/^#\/?/, '').trim().toLowerCase();
  return allowedRoutes.has(route) ? route : 'dashboard';
}

function useHashRoute() {
  const [route, setRoute] = useState(() => normalizeHash(window.location.hash));

  useEffect(() => {
    const handleChange = () => setRoute(normalizeHash(window.location.hash));

    if (!window.location.hash) {
      window.location.hash = '#/dashboard';
    }

    window.addEventListener('hashchange', handleChange);
    return () => window.removeEventListener('hashchange', handleChange);
  }, []);

  function navigate(nextRoute) {
    const target = allowedRoutes.has(nextRoute) ? nextRoute : 'dashboard';
    window.location.hash = `#/${target}`;
  }

  return [route, navigate];
}

export default useHashRoute;

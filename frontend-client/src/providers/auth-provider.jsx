import { createContext, useEffect, useMemo, useState } from 'react';
import { useGetAuthStatus } from '@/hooks';

export const AuthContext = createContext();

export function AuthProvider({ children }) {
  const { data, isLoading } = useGetAuthStatus();
  const [auth, setAuth] = useState({
    isAuthenticated: false,
    userId: null,
    roles: [],
  });

  useEffect(() => {
    if (data) {
      setAuth({
        isAuthenticated: true,
        userId: data.userId,
        roles: data.roles || [],
      });
    }
  }, [isLoading]);

  const value = useMemo(() => ({
    ...auth,
    isLoading: isLoading,
    setAuth,
  }), [auth, isLoading]);

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
}

import { registerPlugin } from '@capacitor/core';

import type { FloatingWindowPlugin } from './definitions';

const FloatingWindow = registerPlugin<FloatingWindowPlugin>('FloatingWindow', {
  web: () => import('./web').then(m => new m.FloatingWindowWeb()),
});

export * from './definitions';
export { FloatingWindow };

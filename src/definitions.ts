export interface FloatingWindowPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
